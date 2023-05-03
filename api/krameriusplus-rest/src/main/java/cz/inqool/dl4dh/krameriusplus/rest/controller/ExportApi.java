package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportFacade;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ExportApi {

    private final ExportFacade facade;

    @Autowired
    public ExportApi(ExportFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "Creates and starts a export of specified export type over a set of publication ids")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/export")
    public ExportRequestDto export(@Valid @RequestBody ExportRequestCreateDto requestDto) {
        return facade.export(requestDto);
    }

    @Operation(summary = "Find an export request.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ExportRequestDto find(@PathVariable String id) {
        return facade.find(id);
    }

    @Operation(summary = "List all export requests.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/list")
    public Result<ExportRequestDto> list(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "owner", required = false) String owner,
                                         @RequestParam(value = "isFinished", required = false) Boolean isFinished) {
        return facade.list(name, owner, isFinished, page, pageSize);
    }

    // DISABLED until real testing on EnrichmentRequests will show that it works
//    @Operation(summary = "Cancel an export request.")
//    @ApiResponse(responseCode = "200", description = "Export request successfully cancelled.")
//    @PostMapping("/{id}/cancel")
//    public ExportRequestDto cancel(@PathVariable String id) {
//        return facade.cancel(id);
//    }
}
