package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.dao.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.ExportRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ExportRepository exportRepository;
    private final Map<ExportFormat, Exporter> exporters = new HashMap<>();

    @Autowired
    public ExporterService(List<Exporter> exporters, ExportRepository exportRepository) {
        this.exportRepository = exportRepository;
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

    public List<Export> list() {
        return exportRepository.findAll();
    }
}
