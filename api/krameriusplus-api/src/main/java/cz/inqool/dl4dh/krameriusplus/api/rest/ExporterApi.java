package cz.inqool.dl4dh.krameriusplus.api.rest;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.facade.ExporterFacade;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Norbert Bodnar
 */
@Validated
@Tag(name = "Export", description = "Exportování")
@RestController
@RequestMapping("/api/exports")
public class ExporterApi {

    private final ExporterFacade exporterFacade;

    @Autowired
    public ExporterApi(ExporterFacade exporterFacade) {
        this.exporterFacade = exporterFacade;
    }

    @Operation(summary = "Create and start a new job of type EXPORT for TEI format. Job is started asynchronously. " +
            "TEI format has it's own endpoint because it has it's own type of parameters.")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/{id}/tei")
    public JobEventDto exportTei(@PathVariable("id") String publicationId,
                                 @RequestBody(required = false) TeiParams params) {
        return exporterFacade.exportTei(publicationId, params);
    }

    @Operation(summary = "Create and start a new job of type EXPORT for formats other then TEI. Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/{id}/{format}")
    public JobEventDto export(@PathVariable("id") String publicationId,
                              @Schema(allowableValues = {"json", "csv", "tsv"})
                              @PathVariable("format") String stringFormat,
                              @RequestBody(required = false) Params params) {
        return exporterFacade.export(publicationId, stringFormat, params);
    }

    @Operation(summary = "List exports.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/list")
    public QueryResults<Export> listExports(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @Schema(description = "Optional publicationId parameter. When provided, only export " +
                                            "for the given publication will be returned.")
                                                @RequestParam(value = "publicationId", required = false) String publicationId) {
        return exporterFacade.list(publicationId, page, pageSize);
    }

    @Operation(summary = "Download export.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") String fileRefId) {
        FileRef file = exporterFacade.getFile(fileRefId);

        file.open();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName()+"\"")
                .header("Content-Length", String.valueOf(file.getSize()))
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new InputStreamResource(file.getStream()));
    }
}
