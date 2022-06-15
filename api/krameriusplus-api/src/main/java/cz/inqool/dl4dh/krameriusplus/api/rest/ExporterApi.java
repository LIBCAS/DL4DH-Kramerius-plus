package cz.inqool.dl4dh.krameriusplus.api.rest;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.AltoExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.CsvExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.JsonExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.TeiExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.facade.ExporterFacade;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export.AltoExportJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export.CsvExportJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export.JsonExportJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export.TeiExportJobConfigDto;
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

import javax.validation.Valid;

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

    @Operation(summary = "Creates and starts a new job of type EXPORT_TEI, which generates an Export in TEI format. " +
            "Job is started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/{id}/tei")
    public JobEventDto exportTei(@PathVariable("id") String publicationId,
                                 @RequestBody @Valid TeiExportJobConfigDto config) {
        return exporterFacade.export(new TeiExportRequestDto(publicationId, config));
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_JSON, which generates an Export in JSON format. " +
            "Job is started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/{id}/json")
    public JobEventDto export(@PathVariable("id") String publicationId,
                              @RequestBody @Valid JsonExportJobConfigDto config) {
        return exporterFacade.export(new JsonExportRequestDto(publicationId, config));
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_CSV, which generates an Export in CSV format. " +
            "Allows to specify 'delimiter', which should be used. Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/{id}/csv")
    public JobEventDto export(@PathVariable("id") String publicationId,
                              @RequestBody @Valid CsvExportJobConfigDto config) {
        return exporterFacade.export(new CsvExportRequestDto(publicationId, config));
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_ALTO, which generates an Export in ALTO format. " +
            "Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/{id}/alto")
    public JobEventDto export(@PathVariable("id") String publicationId,
                              @RequestBody @Valid AltoExportJobConfigDto config) {
        return exporterFacade.export(new AltoExportRequestDto(publicationId, config));
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
