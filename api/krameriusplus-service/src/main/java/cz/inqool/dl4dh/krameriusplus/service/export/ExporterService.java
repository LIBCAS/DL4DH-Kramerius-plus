package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.dao.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.ExportRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.FileRefRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
    private final ExportRepository exportRepository;

    private final FileService fileService;

    private final Map<ExportFormat, Exporter> exporters = new HashMap<>();

    private final static String CRON_EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * ?";
    // for debugging purposes
    private final static String CRON_EVERY_MINUTE = "0 * * * * ?";

    @Autowired
    public ExporterService(List<Exporter> exporters, ExportRepository exportRepository, FileRefRepository fileRefRepository, FileService fileService) {
        this.exportRepository = exportRepository;
        this.fileService = fileService;
        for (Exporter exporter : exporters) {
            this.exporters.put(exporter.getFormat(), exporter);
        }
    }

    @Transactional
    public Export export(String publicationId, Params params, ExportFormat format) {
        Export export = exporters.get(format).export(publicationId, params);
        exportRepository.save(export);

        return export;
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

        exportRepository.save(export);
    }

    public List<Export> list() {
        return exportRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
    }

    public List<Export> listToDelete() {
        return exportRepository.findAllToDelete(Instant.now().minus(DELETE_AFTER_HOURS, ChronoUnit.HOURS));
    }

    @Autowired
    public void setDELETE_AFTER(@Value("${export.auto-delete.afterHours:24}") int hoursLimit) {
        this.DELETE_AFTER_HOURS = hoursLimit;
    }
}
