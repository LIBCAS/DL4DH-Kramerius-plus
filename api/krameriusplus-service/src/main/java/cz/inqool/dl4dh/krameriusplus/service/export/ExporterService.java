package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

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

    private final TeiConnector teiConnector;

    @Autowired
    public ExporterService(PublicationService publicationService, ObjectMapper objectMapper, TeiConnector teiConnector) {
        this.publicationService = publicationService;
        this.objectMapper = objectMapper;
        this.teiConnector = teiConnector;

        File exportDirectory = new File(DEFAULT_EXPORT_FOLDER);
        if (!exportDirectory.exists()) {
            exportDirectory.mkdir();
        }
    }

    @Transactional
    @Async
    public void export(String publicationId, ExportFormat format, Params params) {
        Publication publication = publicationService.findWithPages(publicationId, ALL_PAGES);

        try {
            switch (format) {
                case JSON:
                    exportToJson(publication);
                    break;
                case TEI:
                    exportToTei(publication, params);
            }
        } catch (Exception e) {
            log.error("Error exporting to file", e);
        }

        log.info("Export of '" + publication.getTitle() + "' finished");
    }

    private void exportToTei(Publication publication, Params params) throws IOException {
        if (publication instanceof PagesAware) {
            exportToTei((PagesAware) publication, params);
        } else {
            log.error("Publication {} doesn't have pages directly", publication.getId());
        }
    }

    private void exportToTei(PagesAware publication, Params params) throws IOException {
        String fullTei = teiConnector.merge(publication.getTeiHeader(),
                publication
                        .getPages()
                        .stream()
                        .map(Page::getTeiBody)
                        .collect(Collectors.toList()),
                params);

        File file = createFile(publication.getId());

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(objectMapper.writeValueAsString(fullTei));
            fileWriter.flush();
        }
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
        JSON,
        TEI
    }
}
