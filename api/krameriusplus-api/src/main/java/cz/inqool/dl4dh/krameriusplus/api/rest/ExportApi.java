package cz.inqool.dl4dh.krameriusplus.api.rest;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.AltoExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.CsvExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.JsonExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.TeiExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.TextExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.facade.ExportFacade;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Norbert Bodnar
 */
@Validated
@Tag(name = "Export", description = "Exportování")
@RestController
@RequestMapping("/api/exports")
public class ExportApi {

    private final ExportFacade exportFacade;

    @Autowired
    public ExportApi(ExportFacade exportFacade) {
        this.exportFacade = exportFacade;
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_TEI, which generates an Export in TEI format. " +
            "Job is started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/tei")
    public BulkExportDto export(@RequestBody @Valid TeiExportRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_JSON, which generates an Export in JSON format. " +
            "Job is started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/json")
    public BulkExportDto export(@RequestBody @Valid JsonExportRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_CSV, which generates an Export in CSV format. " +
            "Allows to specify 'delimiter', which should be used. Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/csv")
    public BulkExportDto export(@RequestBody @Valid CsvExportRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_ALTO, which generates an Export in ALTO format. " +
            "Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/alto")
    public BulkExportDto export(@RequestBody @Valid AltoExportRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_TEXT, which generates an Export in TEXT format. " +
            "Text is extracted from ALTO format. Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/text")
    public BulkExportDto export(@RequestBody @Valid TextExportRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "List exports.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/list")
    public QueryResults<Export> listExports(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @Schema(description = "Optional publicationId parameter. When provided, only export " +
                                            "for the given publication will be returned.")
                                                @RequestParam(value = "publicationId", required = false) String publicationId) {
        return exportFacade.list(publicationId, page, pageSize);
    }

    @Operation(summary = "Find export by JobEventId")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ExportDto findByJobEvent(@RequestParam(value = "jobEventId") String jobEventId) {
        return exportFacade.findByJobEvent(jobEventId);
    }

    @Operation(summary = "Find a bulk export by JobEventId")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/bulk")
    public BulkExportDto findBulkByJobEvent(@RequestParam(value = "jobEventId") String jobEventId) {
        return exportFacade.findBulkExport(jobEventId);
    }

    @Operation(summary = "Download export.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") String fileRefId) {
        FileRef file = exportFacade.getFile(fileRefId);

        file.open();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName()+"\"")
                .header("Content-Length", String.valueOf(file.getSize()))
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new InputStreamResource(file.getStream()));
    }
}
