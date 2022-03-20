package cz.inqool.dl4dh.krameriusplus.core.system.export.exporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@Component
public class JsonExporter extends AbstractExporter {

    @Autowired
    public JsonExporter(ObjectMapper objectMapper, FileService fileService, PublicationService publicationService) {
        super(objectMapper, fileService, publicationService);
    }

    @Override
    public FileRef generateFile(Publication publication) {
        try {
            byte[] content = objectMapper.writeValueAsBytes(publication);

            try (InputStream is = new ByteArrayInputStream(content)) {
                return fileService.create(
                        is,
                        content.length,
                        getFormat().getFileName(publication.getId()),
                        ContentType.APPLICATION_JSON.getMimeType());
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not write publication to string.");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Export export(String publicationId, Params params) {
        Publication publication = publicationService.findWithPages(publicationId, params);

        try {
            byte[] content = objectMapper.writeValueAsBytes(publication);

            FileRef file = fileService.create(new ByteArrayInputStream(content), content.length,
                    getFormat().getFileName(publicationId), ContentType.APPLICATION_JSON.getMimeType());

            return createExport(publication, file);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not write publication to string.");
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.JSON;
    }
}
