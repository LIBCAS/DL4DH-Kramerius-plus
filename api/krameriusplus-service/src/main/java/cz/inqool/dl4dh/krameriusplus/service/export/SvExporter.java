package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.lindat.udpipe.Token;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.domain.entity.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.export.ExportFormat.TSV;

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
            CSVFormat pagesCsvFormat = CSVFormat.Builder.create(baseCsvFormat).setHeader("file", "publication_id", "id", "order", "title").build();
            CSVPrinter pagesPrinter = new CSVPrinter(pagesBuffer, pagesCsvFormat);

                int pageIndex = 1;
                for (Page page : publication.getPages()) {
                    String pageFileName = "page_"+publicationId.replace("uuid:", "")+"_"+pageIndex+"_"+page.getId().replace("uuid:", "")+filesExtension;
                    pagesPrinter.printRecord(pageFileName, publicationId, page.getId(), pageIndex++, page.getTitle());

                    // Add page content
                    zip.putNextEntry(new ZipEntry(pageFileName));
                    zip.write(generateCSVPage(page, baseCsvFormat).getBytes());
                    zip.closeEntry();
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
        List<String> columns = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        List<Map<String, Object>> rows = List.of(row);

        columns.add("id");
        row.put("id", publication.getId());

        columns.add("title");
        row.put("title", publication.getTitle());
        // Mods metadata
        if (publication.getModsMetadata() != null) {
            // Mods name
            if (publication.getModsMetadata().getName() != null) {
                columns.add("mods.name.type");
                row.put("mods.name.type", publication.getModsMetadata().getName().getType());
                columns.add("mods.name.identifier");
                row.put("mods.name.identifier", publication.getModsMetadata().getName().getNameIdentifier());
                columns.add("mods.name.part");
                row.put("mods.name.part", publication.getModsMetadata().getName().getNamePart());
            }
            columns.add("mods.genre");
            row.put("mods.genre", publication.getModsMetadata().getGenre());

            // Mods identifiers
            int identifierIndex = 0;
            for (ModsMetadata.Identifier identifier : publication.getModsMetadata().getIdentifiers()) {
                String key = "mods.identifier["+identifierIndex+"]";

                columns.add(key+".type");
                row.put(key+".type", identifier.getType());
                columns.add(key+".invalid");
                row.put(key+".invalid", identifier.getInvalid());
                columns.add(key+".value");
                row.put(key+".value", identifier.getValue());
                identifierIndex += 1;
            }

            // Mods origin info
            if (publication.getModsMetadata().getOriginInfo() != null) {
                columns.add("mods.originInfo.publisher");
                row.put("mods.originInfo.publisher", publication.getModsMetadata().getOriginInfo().getPublisher());

                int placeIndex = 0;
                for (ModsMetadata.OriginInfo.Place place : publication.getModsMetadata().getOriginInfo().getPlaces()) {
                    String key = "mods.originInfo.place["+placeIndex+"]";

                    columns.add(key+".type");
                    row.put(key+".type", place.getType());
                    columns.add(key+".authority");
                    row.put(key+".authority", place.getAuthority());
                    columns.add(key+".type");
                    row.put(key+".type", place.getType());
                    placeIndex += 1;
                }

                int dateIssuedIndex = 0;
                for (ModsMetadata.OriginInfo.DateIssued dateIssued : publication.getModsMetadata().getOriginInfo().getDateIssued()) {
                    String key = "mods.originInfo.dateIssued["+dateIssuedIndex+"]";

                    columns.add(key+".encoding");
                    row.put(key+".encoding", dateIssued.getEncoding());
                    columns.add(key+".point");
                    row.put(key+".point", dateIssued.getPoint());
                    columns.add(key+".value");
                    row.put(key+".value", dateIssued.getValue());
                    dateIssuedIndex += 1;
                }
            }

            // Mods extent
            if (publication.getModsMetadata().getPhysicalDescription() != null) {
                columns.add("mods.physicalDescription.extent");
                row.put("mods.physicalDescription.extent", publication.getModsMetadata().getPhysicalDescription().getExtent());
            }
        }

        StringBuilder buffer = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(buffer, baseCsvFormat);
        printer.printRecord(columns);
        for (Map<String, Object> r : rows) {
            printer.printRecord(
                    columns.stream().map(c -> r.getOrDefault(c, "")
            ).collect(Collectors.toList()));
        }
        return buffer.toString();
    }

    private String generateCSVPage(Page page, CSVFormat baseCsvFormat) throws IOException {
        List<String> header = new ArrayList<>(List.of("page_id", "token", "lemma", "position", "nameTag"));
        Set<String> featsColumns = new TreeSet<>();
        Set<String> miscColumns = new TreeSet<>();
        page.getTokens().forEach(token -> {
            if (token.getLinguisticMetadata().getFeats() != null) {
                for (String fv : token.getLinguisticMetadata().getFeats().split("\\|")) {
                    featsColumns.add(fv.split("=", 2)[0]);
                }
            }
            if (token.getLinguisticMetadata().getMisc() != null) {
                for (String mv : token.getLinguisticMetadata().getMisc().split("\\|")) {
                    miscColumns.add(mv.split("=", 2)[0]);
                }
            }
        });
        featsColumns.forEach(f -> header.add("udpipe.feats."+f));
        miscColumns.forEach(m -> header.add("udpipe.misc."+m));

        StringBuilder buffer = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(buffer, baseCsvFormat);
        printer.printRecord(header);

        for (Token token : page.getTokens()) {
            List<Object> line = new ArrayList<>();
            line.add(page.getId());
            line.add(token.getContent());
            line.add(token.getLinguisticMetadata().getLemma() != null ? token.getLinguisticMetadata().getLemma() : "");
            line.add(token.getLinguisticMetadata().getPosition() != null ? token.getLinguisticMetadata().getPosition() : "");
            line.add(token.getNameTagMetadata());
            if (token.getLinguisticMetadata().getFeats() != null) {
                Map<String, String> featsValues = Arrays.stream(token.getLinguisticMetadata().getFeats().split("\\|"))
                        .map(i -> i.split("=", 2))
                        .collect(Collectors.toMap(a -> a[0], a -> a[1]));
                featsColumns.forEach(f -> line.add(featsValues.getOrDefault(f, "")));
            }
            if (token.getLinguisticMetadata().getMisc() != null) {
                Map<String, String> miscValues = Arrays.stream(token.getLinguisticMetadata().getMisc().split("\\|"))
                        .map(i -> i.split("=", 2))
                        .collect(Collectors.toMap(a -> a[0], a -> a[1]));
                miscColumns.forEach(m -> line.add(miscValues.getOrDefault(m, "")));
            }
            printer.printRecord(line);
        }
        return buffer.toString();
    }
}
