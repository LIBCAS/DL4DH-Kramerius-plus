package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService.ALL_PAGES;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class ExporterService {

    private final PublicationService publicationService;
    private final Map<ExportFormat, Exporter> exporters = new HashMap<>();


    @Autowired
    public ExporterService(List<Exporter> exporters, PublicationService publicationService) {
        for (Exporter exporter : exporters) {
            this.exporters.put(exporter.getFormat(), exporter);
        }

        this.publicationService = publicationService;
    }

    @Transactional
    @Async
    public void export(String publicationId, Params params) {
        Publication publication = publicationService.findWithPages(publicationId, ALL_PAGES);

        try {
            exporters.get(params.getFormat()).export(publication, params);
        } catch (Exception e) {
            log.error("Error exporting to file", e);
        }

        log.info("Export of '" + publication.getTitle() + "' finished");
    }
}
