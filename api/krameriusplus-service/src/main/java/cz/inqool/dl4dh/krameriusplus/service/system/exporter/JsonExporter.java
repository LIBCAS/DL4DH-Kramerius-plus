package cz.inqool.dl4dh.krameriusplus.service.system.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JsonExporter {

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonExporter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void export(Publication publication, OutputStream outputStream) {
        doWrite(publication, outputStream);
    }

    public void export(Page page, OutputStream outputStream) {
        doWrite(page, outputStream);
    }

    private void doWrite(DigitalObject digitalObject, OutputStream destination) {
        try {
            objectMapper.writeValue(destination, digitalObject);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write digitalObject to the given outputStream.");
        }
    }
}
