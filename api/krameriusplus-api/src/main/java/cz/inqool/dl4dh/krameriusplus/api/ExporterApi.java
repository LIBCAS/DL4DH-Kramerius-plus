package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.service.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
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

    @PostMapping("/{id}/{format}")
    public void exportToJson(@PathVariable("id") String publicationId, @PathVariable("format") String format) {
        Params params = new Params();
        params.setFormat(ExportFormat.fromString(format));

        exporterService.export(publicationId, params);
    }
//
//    @GetMapping("/download/{id}")
//    public ResponseEntity<InputStreamResource> download() {
//
//    }
}
