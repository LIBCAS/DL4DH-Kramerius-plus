package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions.rest.RestException;
import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.JobPlanResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.*;
import cz.inqool.dl4dh.krameriusplus.api.facade.EnrichmentFacadeImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@Tag(name = "Enrichment", description = "Obohacení")
@RestController
@RequestMapping("/api/enrich")
public class EnrichmentApi {

    private final EnrichmentFacadeImpl facade;

    @Autowired
    public EnrichmentApi(EnrichmentFacadeImpl facade) {
        this.facade = facade;
    }

    @PostMapping(value = "/plan", produces = APPLICATION_JSON_VALUE)
    public JobPlanResponseDto enrich(@Valid @RequestBody ExecutionPlanRequestDto requestDto) {
        return facade.enrichWithPlan(requestDto);
    }

    @Operation(summary = "Create and start new jobs of type DOWNLOAD_K_STRUCTURE. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @ApiResponse(responseCode = "400", description = "Some publications were already enriched and 'override' parameter " +
            "was not set to true",
            content = @Content(schema = @Schema(implementation = RestException.class), mediaType = APPLICATION_JSON_VALUE))
    @PostMapping(value = "/download-k-structure", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto downloadKStructure(@RequestBody @Valid DownloadKStructureRequestDto requestDto) {
        return facade.enrich(requestDto);
    }

    @Operation(summary = "Create and start new jobs of type ENRICH_EXTERNAL. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @PostMapping(value = "/enrich-external", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichExternal(@RequestBody @Valid EnrichExternalRequestDto requestDto) {
        return facade.enrich(requestDto);
    }

    @Operation(summary = "Create and start new jobs of type ENRICH_NDK. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @PostMapping(value = "/enrich-ndk", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichNdk(@RequestBody @Valid EnrichNdkRequestDto requestDto) {
        return facade.enrich(requestDto);
    }

    @Operation(summary = "Create and start new jobs of type ENRICH_TEI. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @PostMapping(value = "/enrich-tei", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichTei(@RequestBody @Valid EnrichTeiRequestDto requestDto) {
        return facade.enrich(requestDto);
    }
}
