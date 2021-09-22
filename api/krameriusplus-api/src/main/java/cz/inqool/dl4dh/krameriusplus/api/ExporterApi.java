package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import cz.inqool.dl4dh.krameriusplus.service.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.service.export.FileService;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("/api/export")
public class ExporterApi {

    private final ExporterService exporterService;
    private final FileService fileService;

    @Autowired
    public ExporterApi(ExporterService exporterService, FileService fileService) {
        this.exporterService = exporterService;
        this.fileService = fileService;
    }

    @Operation(summary = "Generate an export from an enriched publication")
    @PostMapping("/{id}/{format}")
    public Export export(@PathVariable("id") String publicationId, @PathVariable("format") ExportFormat format) {
        Params params = new Params();
        params.setFormat(format);

        return exporterService.export(publicationId, params);
    }

    @Operation(summary = "Get a list of all generated exports")
    @GetMapping("/list")
    public List<Export> listExports() {
        return exporterService.list();
    }

    @Operation(summary = "Download a selected generated export")
    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") String fileRefId) {
        FileRef file = fileService.find(fileRefId);

        file.open();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName()+"\"")
                .header("Content-Length", String.valueOf(file.getSize()))
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new InputStreamResource(file.getStream()));
    }
}
