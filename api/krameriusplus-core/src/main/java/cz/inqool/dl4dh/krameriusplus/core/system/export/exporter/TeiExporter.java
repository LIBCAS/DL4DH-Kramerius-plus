package cz.inqool.dl4dh.krameriusplus.core.system.export.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.ExportException;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.ExportException.ErrorCode.TEI_MERGE_ERROR;

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
    public FileRef generateFile(Publication publication) {
//        File tmpFile = getTei(publication, teiParams);
//
//        try (InputStream is = new FileInputStream(tmpFile)) {
//            FileRef file = fileService.create(, tmpFile.length(),
//                    getFormat().getFileName(publicationId), ContentType.TEXT_XML.getMimeType());
//
//            return createExport(publication, file);
//        } catch (FileNotFoundException e) {
//            throw new ExportException(TEI_MERGE_ERROR, e);
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Export export(String publicationId, Params params) {
        TeiParams teiParams = convert(params);

        Publication publication = publicationService.findWithPages(publicationId, teiParams.cleanForTei());

        File tmpFile = getTei(publication, teiParams);

        try {
            FileRef file = fileService.create(new FileInputStream(tmpFile), tmpFile.length(),
                    getFormat().getFileName(publicationId), ContentType.TEXT_XML.getMimeType());

            return createExport(publication, file);
        } catch (FileNotFoundException e) {
            throw new ExportException(TEI_MERGE_ERROR, e);
        }
    }

    private TeiParams convert(Params params) {
        if (params instanceof TeiParams) {
            return (TeiParams) params;
        } else {
            return new TeiParams(params);
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.TEI;
    }

    private File getTei(Publication publication, TeiParams params) {
//
//        return teiConnector.merge(publication.getTeiHeader(),
//                publication
//                        .getPages()
//                        .stream()
//                        .map(Page::getTeiBody)
//                        .filter(Objects::nonNull)
//                        .collect(Collectors.toList()),
//                params);
        throw new UnsupportedOperationException("Not implemented");
    }
}
