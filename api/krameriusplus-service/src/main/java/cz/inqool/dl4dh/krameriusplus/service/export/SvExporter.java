package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.dao.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Token;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cz.inqool.dl4dh.krameriusplus.domain.dao.ExportFormat.TSV;

@Slf4j
public abstract class SvExporter extends AbstractExporter {
    private final ExportFormat format;

    public SvExporter(ObjectMapper objectMapper, FileService fileService, PublicationService publicationService, ExportFormat format) {
        super(objectMapper, fileService, publicationService);
        this.format = format;
    }

    @Override
    public Export export(String publicationId, Params params) {
        Publication publication = publicationService.findWithPages(publicationId, params);

        CSVFormat baseCsvFormat = format.equals(TSV) ? CSVFormat.TDF : CSVFormat.DEFAULT;
        String filesExtension = format.equals(TSV) ? ".tsv" : ".csv";
        try {
            // Create zip
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bout);

            // Add metadata
            zip.putNextEntry(new ZipEntry("metadata"+filesExtension));
            zip.write(generateCSVMetadata(publication, baseCsvFormat).getBytes());
            zip.closeEntry();

            // Create pages
            StringBuilder pagesBuffer = new StringBuilder();
            CSVFormat pagesCsvFormat = CSVFormat.Builder.create(baseCsvFormat).setHeader("File", "ID", "Title", "PageNumber").build();
            CSVPrinter pagesPrinter = new CSVPrinter(pagesBuffer, pagesCsvFormat);

            if (publication instanceof PagesAware) {
                for (Page page : ((PagesAware) publication).getPages()) {
                    String pageFileName = page.getId().replace("uuid:", "")+filesExtension;
                    pagesPrinter.printRecord(pageFileName, page.getId(), page.getTitle(), page.getPageNumber());

                    // Add page content
                    zip.putNextEntry(new ZipEntry(pageFileName));
                    zip.write(generateCSVPage(page, baseCsvFormat).getBytes());
                    zip.closeEntry();
                }
            }

            // Add pages list
            zip.putNextEntry(new ZipEntry("pages"+filesExtension));
            zip.write(pagesBuffer.toString().getBytes());
            zip.closeEntry();

            zip.close();
            byte[] output = bout.toByteArray();
            FileRef file = fileService.create(new ByteArrayInputStream(output), output.length,
                    getFormat().getFileName(publicationId), "application/zip");

            return createExport(publication, file);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write publication to string.");
        }
    }

    @Override
    public ExportFormat getFormat() {
        return format;
    }

    private String generateCSVMetadata(Publication publication, CSVFormat baseCsvFormat) throws IOException {
        StringBuilder buffer = new StringBuilder();
        CSVFormat csvFormat = CSVFormat.Builder.create(baseCsvFormat).setHeader("Metadata", "Value").build();
        CSVPrinter printer = new CSVPrinter(buffer, csvFormat);

        printer.printRecord("id", publication.getId());
        printer.printRecord("title", publication.getTitle());
        // Mods metadata
        if (publication.getModsMetadata() != null) {
            // Mods name
            if (publication.getModsMetadata().getName() != null) {
                printer.printRecord("mods.name.type", publication.getModsMetadata().getName().getType());
                printer.printRecord("mods.name.identifier", publication.getModsMetadata().getName().getNameIdentifier());
                printer.printRecord("mods.name.part", publication.getModsMetadata().getName().getNamePart());
            }
            printer.printRecord("mods.genre", publication.getModsMetadata().getGenre());

            // Mods identifiers
            int identifierIndex = 0;
            for (ModsMetadata.Identifier identifier : publication.getModsMetadata().getIdentifiers()) {
                String key = "mods.identifier["+identifierIndex+"]";
                printer.printRecord(key+".type", identifier.getType());
                printer.printRecord(key+".invalid", identifier.getInvalid());
                printer.printRecord(key+".value", identifier.getValue());
                identifierIndex += 1;
            }

            // Mods origin info
            if (publication.getModsMetadata().getOriginInfo() != null) {
                printer.printRecord("mods.originInfo.publisher", publication.getModsMetadata().getOriginInfo().getPublisher());

                int placeIndex = 0;
                for (ModsMetadata.OriginInfo.Place place : publication.getModsMetadata().getOriginInfo().getPlaces()) {
                    String key = "mods.originInfo.place["+placeIndex+"]";
                    printer.printRecord(key+".type", place.getType());
                    printer.printRecord(key+".authority", place.getAuthority());
                    printer.printRecord(key+".value", place.getValue());
                    placeIndex += 1;
                }

                int dateIssuedIndex = 0;
                for (ModsMetadata.OriginInfo.DateIssued dateIssued : publication.getModsMetadata().getOriginInfo().getDateIssued()) {
                    String key = "mods.originInfo.dateIssued["+dateIssuedIndex+"]";
                    printer.printRecord(key+".encoding", dateIssued.getEncoding());
                    printer.printRecord(key+".point", dateIssued.getPoint());
                    printer.printRecord(key+".value", dateIssued.getValue());
                    dateIssuedIndex += 1;
                }
            }

            // Mods extent
            if (publication.getModsMetadata().getPhysicalDescription() != null) {
                printer.printRecord("mods.physicalDescription.extent", publication.getModsMetadata().getPhysicalDescription().getExtent());
            }
        }

        return buffer.toString();
    }

    private String generateCSVPage(Page page, CSVFormat baseCsvFormat) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String[] header = {"Token", "Lemma", "Position", "Feats", "Misc", "NameTag"};
        CSVFormat csvFormat = CSVFormat.Builder.create(baseCsvFormat).setHeader(header).build();
        CSVPrinter printer = new CSVPrinter(buffer, csvFormat);

        for (Token token : page.getTokens()) {
            printer.printRecord(token.getContent(),
                    token.getLinguisticMetadata().getLemma(),
                    token.getLinguisticMetadata().getPosition(),
                    token.getLinguisticMetadata().getFeats(),
                    token.getLinguisticMetadata().getMisc(),
                    token.getNameTagMetadata());
        }
        return buffer.toString();
    }
}
