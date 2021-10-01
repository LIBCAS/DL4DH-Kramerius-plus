package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import cz.inqool.dl4dh.krameriusplus.domain.dao.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.service.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.service.export.FileService;
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

    @PostMapping("/{id}/{format}")
    public Export export(@PathVariable("id") String publicationId,
                          @PathVariable("format") String format,
                          @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }

        return exporterService.export(publicationId, params, ExportFormat.fromString(format));
    }

    @GetMapping("/list")
    public List<Export> listExports() {
        return exporterService.list();
    }

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
