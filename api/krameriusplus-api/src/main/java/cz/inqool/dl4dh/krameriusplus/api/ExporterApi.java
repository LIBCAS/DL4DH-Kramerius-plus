package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.service.export.ExporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//
//    @GetMapping("/download/{id}")
//    public ResponseEntity<InputStreamResource> download() {
//
//    }
}
