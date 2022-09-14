package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.api.facade.EnrichmentFacade;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @Operation(summary = "Create a more complex plan for running multiple jobs for multiple publications in the specified order. " +
            "For every publicationId, a new JobPlan will be created, consisting of multiple JobEvents. For every created JobPlan, " +
            "a new JobEvent is created for every configuration in the given configs array(in order in which they were received). " +
            "Every created JobPlan is then enqueued to run. When run, it's first JobEvent will be run. After it successfully finishes, " +
            "the next JobEvent will be run, and so on.")
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
    public List<EnrichmentRequestDto> list() {
        return facade.list();
    }
}
