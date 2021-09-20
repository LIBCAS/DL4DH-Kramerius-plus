package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.export.ExportFormat.JSON;

@Component
public class JsonExporter extends AbstractExporter {

    @Autowired
    public JsonExporter(ObjectMapper objectMapper, FileService fileService, PublicationService publicationService) {
        super(objectMapper, fileService, publicationService);
    }

    @Override
    public FileRef export(String publicationId, Params params) {
        Publication publication = publicationService.findWithPages(publicationId, PublicationService.ALL_PAGES);

        try {
            String content = objectMapper.writeValueAsString(publication);

            return fileService.create(publication, content, JSON);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not write publication to string.");
        }
    }

    @Override
    public ExportFormat getFormat() {
        return JSON;
    }
}
