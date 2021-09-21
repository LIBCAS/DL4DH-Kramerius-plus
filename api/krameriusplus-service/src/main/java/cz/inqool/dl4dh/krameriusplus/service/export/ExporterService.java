package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class ExporterService {

    private final Map<ExportFormat, Exporter> exporters = new HashMap<>();

    @Autowired
    public ExporterService(List<Exporter> exporters) {
        for (Exporter exporter : exporters) {
            this.exporters.put(exporter.getFormat(), exporter);
        }
    }

    @Transactional
    public FileRef export(String publicationId, Params params) {
        return exporters.get(params.getFormat()).export(publicationId, params);
    }
}
