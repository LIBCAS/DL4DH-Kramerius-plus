package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JsonExporter extends AbstractExporter {

    @Autowired
    public JsonExporter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public File export(Publication publication, Params params) {
        File file = createFile(publication.getId());

        try {
            writeToFile(objectMapper.writeValueAsString(publication), file);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not write publication to string.");
        }

        return file;
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.JSON;
    }
}
