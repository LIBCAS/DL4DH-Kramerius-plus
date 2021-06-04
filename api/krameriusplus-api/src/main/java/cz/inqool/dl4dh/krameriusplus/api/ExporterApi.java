package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.service.export.ExporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("/api/export")
public class ExporterApi {

    private final ExporterService exporterService;

    @Autowired
    public ExporterApi(ExporterService exporterService) {
        this.exporterService = exporterService;
    }

    @PostMapping("/{id}")
    public void export(@PathVariable("id") String publicationId) {
        exporterService.export(publicationId, ExporterService.ExportFormat.JSON);
    }
}
