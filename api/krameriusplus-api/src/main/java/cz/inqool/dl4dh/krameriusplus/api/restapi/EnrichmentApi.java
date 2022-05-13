package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions.rest.RestException;
import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.DownloadKStructureRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichExternalRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichNdkRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichTeiRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.WebClientDataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.DownloadKStructureCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.JobEventCreateDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException.ErrorCode.ALREADY_ENRICHED;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.isTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@Tag(name = "Enrichment", description = "Obohacení")
@RestController
@RequestMapping("/api/enrich")
public class EnrichmentApi {

    private final PublicationService publicationService;

    private final JobEventService jobEventService;

    private final WebClientDataProvider krameriusDataProvider;

    @Autowired
    public EnrichmentApi(PublicationService publicationService,
                         JobEventService jobEventService,
                         WebClientDataProvider krameriusDataProvider) {
        this.publicationService = publicationService;
        this.jobEventService = jobEventService;
        this.krameriusDataProvider = krameriusDataProvider;
    }

    @Operation(summary = "Create and start new jobs of type DOWNLOAD_K_STRUCTURE. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @ApiResponse(responseCode = "400", description = "Some publications were already enriched and 'override' parameter " +
            "was not set to true",
            content = @Content(schema = @Schema(implementation = RestException.class), mediaType = APPLICATION_JSON_VALUE))
    @PostMapping(value = "/download-k-structure", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto downloadKStructure(@RequestBody DownloadKStructureRequestDto requestDto) {
        List<String> alreadyEnrichedPublications = collectAlreadyEnriched(requestDto.getPublications());

        isTrue(requestDto.isOverride() || alreadyEnrichedPublications.isEmpty(),
                () -> new SchedulingException(ALREADY_ENRICHED,
                        "Publications " + alreadyEnrichedPublications + " are already enriched, to override, repeat request with request parameter" +
                                " 'override=true'"));

        validateIdentifiers(requestDto.getPublications());

        return createAndReturnResponse(requestDto.getPublications());
    }

    @Operation(summary = "Create and start new jobs of type ENRICH_EXTERNAL. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @PostMapping(value = "/enrich-external", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichExternal(@RequestBody EnrichExternalRequestDto requestDto) {
        return createAndReturnResponse(requestDto.getPublications());
    }

    @Operation(summary = "Create and start new jobs of type ENRICH_NDK. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @PostMapping(value = "/enrich-ndk", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichNdk(@RequestBody EnrichNdkRequestDto requestDto) {
        return createAndReturnResponse(requestDto.getPublications());
    }

    @Operation(summary = "Create and start new jobs of type ENRICH_TEI. Jobs are started asynchronously.")
    @ApiResponse(responseCode = "200", description = "Jobs successfully created")
    @PostMapping(value = "/enrich-tei", produces = APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichTei(@RequestBody EnrichTeiRequestDto requestDto) {
        return createAndReturnResponse(requestDto.getPublications());
    }

    private <T extends JobEventCreateDto> EnrichResponseDto createAndReturnResponse(Set<T> jobCreateDto) {
        EnrichResponseDto responseDto = new EnrichResponseDto();

        for (T createDto : jobCreateDto) {
            JobEventDto jobEventDto = jobEventService.create(createDto);
            responseDto.getEnrichJobs().add(jobEventDto);
            jobEventService.enqueueJob(jobEventDto.getId());
        }

        return responseDto;
    }

    private List<String> collectAlreadyEnriched(Set<DownloadKStructureCreateDto> createDtos) {
        List<String> alreadyEnrichedPublications = new ArrayList<>();

        createDtos.forEach(createDto -> {
            if (publicationService.exists(createDto.getPublicationId())) {
                alreadyEnrichedPublications.add(createDto.getPublicationId());
            }
        });

        return alreadyEnrichedPublications;
    }

    private void validateIdentifiers(Set<DownloadKStructureCreateDto> createDtos) {
        for (DownloadKStructureCreateDto createDto : createDtos) {
            krameriusDataProvider.getDigitalObject(createDto.getPublicationId());
        }
    }
}
