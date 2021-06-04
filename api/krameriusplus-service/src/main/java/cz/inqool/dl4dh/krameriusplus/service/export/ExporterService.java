package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import static cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService.ALL_PAGES;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class ExporterService {

    private final PublicationService publicationService;

    private final ObjectMapper objectMapper;

    private final String DEFAULT_EXPORT_FOLDER = "export";

    @Autowired
    public ExporterService(PublicationService publicationService, ObjectMapper objectMapper) {
        this.publicationService = publicationService;
        this.objectMapper = objectMapper;

        File exportDirectory = new File(DEFAULT_EXPORT_FOLDER);
        if (!exportDirectory.exists()) {
            exportDirectory.mkdir();
        }
    }

    @Transactional
    @Async
    public void export(String publicationId, ExportFormat format) {
        Publication publication = publicationService.findWithPages(publicationId, ALL_PAGES);

        try {
            switch (format) {
                case JSON:
                    exportToJson(publication);
            }
        } catch (Exception e) {
            log.error("Error exporting to file", e);
        }

        log.info("Export of '" + publication.getTitle() + "' finished");
    }

    private void exportToJson(Publication publication) throws IOException {
        File file = createFile(publication.getId());

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(objectMapper.writeValueAsString(publication));
            fileWriter.flush();
        }
    }

    private File createFile(String id) {
        return new File(DEFAULT_EXPORT_FOLDER + File.separator + getFileNameFromId(id));
    }

    private String getFileNameFromId(String id) {
        String idWithoutPrefix = id.substring(id.indexOf(':') + 1);

        return "uuid_" + idWithoutPrefix + "_" + LocalDate.now() + ".json";
    }

    public enum ExportFormat {
        JSON
    }
}
