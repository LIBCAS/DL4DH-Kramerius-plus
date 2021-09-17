package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public abstract class AbstractExporter implements Exporter {

    protected final ObjectMapper objectMapper;
    private final String DEFAULT_EXPORT_FOLDER = "exports";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");

    protected AbstractExporter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        File exportDirectory = new File(DEFAULT_EXPORT_FOLDER);
        if (!exportDirectory.exists()) {
            exportDirectory.mkdir();
        }
    }

    protected void  writeToFile(String fileContent, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(fileContent);
            fileWriter.flush();
        } catch (IOException exception) {
            log.error("IOException when writing to file", exception);
        }
    }

    protected File createFile(String id) {
        return new File(DEFAULT_EXPORT_FOLDER + File.separator + getFileNameFromId(id));
    }

    private String getFileNameFromId(String id) {
        String idWithoutPrefix = id.substring(id.indexOf(':') + 1);

        return String.format("uuid_%s_%s.%s", idWithoutPrefix, dateTimeFormatter.format(LocalDate.now()), getFormat().getSuffix());
    }
}
