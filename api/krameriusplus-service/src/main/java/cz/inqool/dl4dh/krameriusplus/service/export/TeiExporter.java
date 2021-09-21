package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.exception.ExportException;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExportException.ErrorCode.TEI_MERGE_ERROR;
import static cz.inqool.dl4dh.krameriusplus.service.export.ExportFormat.TEI;

@Component
@Slf4j
public class TeiExporter extends AbstractExporter {

    private final TeiConnector teiConnector;

    @Autowired
    public TeiExporter(ObjectMapper objectMapper, TeiConnector teiConnector, FileService fileService, PublicationService publicationService) {
        super(objectMapper, fileService, publicationService);
        this.teiConnector = teiConnector;
    }

    @Override
    public Export export(String publicationId, Params params) {
        Publication publication = publicationService.findWithPages(publicationId, PublicationService.ALL_PAGES);

        if (!(publication instanceof PagesAware)) {
            throw new IllegalArgumentException("Tei cannot be generated from a publication which does not have pages");
        }

        PagesAware publicationWithPages = (PagesAware) publication;

        File tmpFile = getTei(publicationWithPages, params);

        try {
            FileRef file = fileService.create(new FileInputStream(tmpFile), tmpFile.length(),
                    getFormat().getFileName(publicationId), ContentType.TEXT_XML.getMimeType());

            return createExport(publication, file);
        } catch (FileNotFoundException e) {
            throw new ExportException(TEI_MERGE_ERROR, e);
        }
    }

    @Override
    public ExportFormat getFormat() {
        return TEI;
    }

    private File getTei(PagesAware publication, Params params) {
        return teiConnector.merge(publication.getTeiHeader(),
                publication
                        .getPages()
                        .stream()
                        .map(Page::getTeiBody)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()),
                params);
    }
}