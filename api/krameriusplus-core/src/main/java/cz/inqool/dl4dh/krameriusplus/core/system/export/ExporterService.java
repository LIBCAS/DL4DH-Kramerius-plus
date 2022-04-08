package cz.inqool.dl4dh.krameriusplus.core.system.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.exporter.Exporter;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class ExporterService {

    private int DELETE_AFTER_HOURS;

    private final ExportStore exportStore;

    private final FileService fileService;

    private final Map<ExportFormat, Exporter> exporters = new HashMap<>();

    private final static String CRON_EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * ?";
    // for debugging purposes
    private final static String CRON_EVERY_MINUTE = "0 * * * * ?";

    @Autowired
    public ExporterService(List<Exporter> exporters, ExportStore exportStore, FileService fileService) {
        this.exportStore = exportStore;
        this.fileService = fileService;
        for (Exporter exporter : exporters) {
            this.exporters.put(exporter.getFormat(), exporter);
        }
    }

    @Transactional
    public void export(String publicationId, Params params, ExportFormat format) {
        Export export = exporters.get(format).export(publicationId, params);
        exportStore.create(export);
    }

    @Scheduled(cron = CRON_EVERY_DAY_AT_MIDNIGHT)
    public void cleanUp() {

        List<Export> exports = listToDelete();

        exports.forEach(this::delete);
    }

    public void delete(Export export) {
        FileRef fileRef = fileService.find(export.getFileRef().getId());

        fileService.delete(fileRef);

        export.setDeleted(Instant.now());
        export.setFileRef(null);

        exportStore.update(export);
    }

    public List<Export> list() {
        return exportStore.listAll();
    }

    public List<Export> listToDelete() {
        return exportStore.listDeletedOlderThan(Instant.now().minus(DELETE_AFTER_HOURS, ChronoUnit.HOURS));
    }

    @Autowired
    public void setDELETE_AFTER(@Value("${export.auto-delete.afterHours:24}") int hoursLimit) {
        this.DELETE_AFTER_HOURS = hoursLimit;
    }
}
