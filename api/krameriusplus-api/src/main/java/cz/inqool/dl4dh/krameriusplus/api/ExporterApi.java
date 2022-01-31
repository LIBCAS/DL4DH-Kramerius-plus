package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import cz.inqool.dl4dh.krameriusplus.domain.entity.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.service.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.service.export.FileService;
import cz.inqool.dl4dh.krameriusplus.service.export.TeiParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.export.ExportFormat.*;

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

    @PostMapping("/{id}/json")
    public Export exportJson(@PathVariable("id") String publicationId,
                          @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }

        return exporterService.export(publicationId, params, JSON);
    }

    @PostMapping("/{id}/tei")
    public Export exportTei(@PathVariable("id") String publicationId,
                            @RequestBody(required = false) TeiParams params) {
        if (params == null) {
            params = new TeiParams();
        }

        return exporterService.export(publicationId, params, TEI);
    }

    @PostMapping("/{id}/csv")
    public Export exportCsv(@PathVariable("id") String publicationId,
                            @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }
        return exporterService.export(publicationId, params, CSV);
    }

    @PostMapping("/{id}/tsv")
    public Export exportTsv(@PathVariable("id") String publicationId,
                            @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }
        return exporterService.export(publicationId, params, TSV);
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
