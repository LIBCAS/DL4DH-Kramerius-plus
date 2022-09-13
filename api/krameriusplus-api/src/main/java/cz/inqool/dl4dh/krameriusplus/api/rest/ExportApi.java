package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.api.dto.export.AltoExportCreationRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.CsvExportCreationRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.JsonExportCreationRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.TeiExportCreationRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.TextExportCreationRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.facade.ExportFacade;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestDto;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
    public ExportRequestDto export(@RequestBody @Valid TeiExportCreationRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_JSON, which generates an Export in JSON format. " +
            "Job is started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/json")
    public ExportRequestDto export(@RequestBody @Valid JsonExportCreationRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_CSV, which generates an Export in CSV format. " +
            "Allows to specify 'delimiter', which should be used. Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/csv")
    public ExportRequestDto export(@RequestBody @Valid CsvExportCreationRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_ALTO, which generates an Export in ALTO format. " +
            "Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/alto")
    public ExportRequestDto export(@RequestBody @Valid AltoExportCreationRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Creates and starts a new job of type EXPORT_TEXT, which generates an Export in TEXT format. " +
            "Text is extracted from ALTO format. Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/text")
    public ExportRequestDto export(@RequestBody @Valid TextExportCreationRequestDto requestDto) {
        return exportFacade.export(requestDto);
    }

    @Operation(summary = "Find an export request.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/{id}")
    public ExportRequestDto find(@PathVariable String id) {
        return exportFacade.find(id);
    }

    @Operation(summary = "List all export requests.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("list")
    public List<ExportRequestDto> list() {
        return exportFacade.listAll();
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
