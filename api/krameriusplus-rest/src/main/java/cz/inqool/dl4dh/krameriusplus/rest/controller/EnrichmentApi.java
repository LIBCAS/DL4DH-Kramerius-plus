package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentFacade;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@Tag(name = "Enrichment", description = "Obohacení")
@RestController
@RequestMapping("/api/enrichment")
public class EnrichmentApi {

    private final EnrichmentFacade facade;

    @Autowired
    public EnrichmentApi(EnrichmentFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "Create an Enrichment request. An EnrichmentRequest consists of a collection of JobPlans, " +
            "where each JobPlan is a sequence of JobEvents for a given publication.")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public EnrichmentRequestDto enrich(@Valid @RequestBody EnrichmentRequestCreateDto requestDto) {
        return facade.enrich(requestDto);
    }

    @Operation(summary = "Find enrichment request by given ID.")
    @GetMapping("/{id}")
    public EnrichmentRequestDto find(@PathVariable String id) {
        return facade.find(id);
    }

    @Operation(summary = "List enrichment requests.")
    @GetMapping("/list")
    public QueryResults<EnrichmentRequestDto> list(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "owner", required = false) String owner) {
        return facade.list(name, owner, page, pageSize);
    }
}
