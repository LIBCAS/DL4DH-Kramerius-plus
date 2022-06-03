package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PublicationFileWriter implements ItemWriter<Publication> {

    private final WritePart writePart;

    private final ObjectMapper objectMapper;

    public PublicationFileWriter(WritePart writePart, ObjectMapper objectMapper) {
        this.writePart = writePart;
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(List<? extends Publication> items) throws Exception {
        if (items.size() != 1) {
            throw new IllegalArgumentException("Can only write a single publication");
        }

        Publication publication = items.get(0);

        File file = new FileSystemResource("tmp_" + ExportFormat.JSON.getFileName(publication.getId())).getFile();

        try (FileWriter fileWriter = new FileWriter(file, writePart == WritePart.AFTER_PAGES)) {
            if (writePart == WritePart.BEFORE_PAGES) {
                writeBeforePart(fileWriter, publication);
            } else {
                writeAfterPart(fileWriter, publication);
            }
        }
    }

    private void writeBeforePart(FileWriter fileWriter, Publication publication) throws IOException {
        fileWriter.write("{\n");
        fileWriter.write("\t" + writeField("id", publication.getId()));
        fileWriter.write("\t" + writeField("title", publication.getTitle()));
        fileWriter.write("\t" + writeField("modsMetadata", publication.getModsMetadata()));
        fileWriter.write("\t" + writeField("ocrParadata", publication.getOcrParadata()));
        fileWriter.write("\t" + writeField("udPipeParadata", publication.getUdPipeParadata()));
        fileWriter.write("\t" + writeField("nameTagParadata", publication.getNameTagParadata()));
        fileWriter.write("\t\"pages\": [\n");
    }

    private void writeAfterPart(FileWriter fileWriter, Publication publication) throws IOException {
        fileWriter.write("\n}");
    }

    private String writeField(String fieldName, Object object) throws JsonProcessingException {
        return String.format("\"%s\": %s, \n", fieldName, objectMapper.writeValueAsString(object));
    }

    private String writeField(String fieldName, String fieldValue) {
        return String.format("\"%s\": \"%s\",\n", fieldName, fieldValue);
    }

    public enum WritePart {
        BEFORE_PAGES,
        AFTER_PAGES
    }
}
