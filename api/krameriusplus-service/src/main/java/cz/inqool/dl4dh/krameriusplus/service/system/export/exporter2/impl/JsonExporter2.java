package cz.inqool.dl4dh.krameriusplus.service.system.export.exporter2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.system.export.exporter2.Exporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JsonExporter2 implements Exporter {

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonExporter2(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void export(DigitalObject digitalObject, OutputStream outputStream) {
        doWrite(digitalObject, outputStream);
    }

    private void doWrite(DigitalObject digitalObject, OutputStream destination) {
        try {
            objectMapper.writeValue(destination, digitalObject);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write digitalObject to the given outputStream.");
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.JSON;
    }
}
