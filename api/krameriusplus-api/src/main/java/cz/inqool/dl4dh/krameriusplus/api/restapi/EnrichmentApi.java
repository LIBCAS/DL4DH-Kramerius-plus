package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.DownloadKStructureRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichExternalRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichNdkRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichTeiRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.WebClientDataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.DownloadKStructureCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.JobEventCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException.ErrorCode.ALREADY_ENRICHED;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.isTrue;

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

    @PostMapping(value = "/download-k-structure", produces = MediaType.APPLICATION_JSON_VALUE)
    public EnrichResponseDto downloadKStructure(@RequestBody @Valid DownloadKStructureRequestDto requestDto) {
        List<String> alreadyEnrichedPublications = collectAlreadyEnriched(requestDto.getPublications());

        isTrue(requestDto.isOverride() || alreadyEnrichedPublications.isEmpty(),
                () -> new SchedulingException(ALREADY_ENRICHED,
                    "Publications " + alreadyEnrichedPublications + " are already enriched, to override, repeat request with request parameter" +
                            " 'override=true'"));

        validateIdentifiers(requestDto.getPublications());

        return createAndReturnResponse(requestDto.getPublications());
    }

    @PostMapping(value = "/enrich-external", produces = MediaType.APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichExternal(@RequestBody @Valid EnrichExternalRequestDto requestDto) {
        return createAndReturnResponse(requestDto.getPublications());
    }

    @PostMapping(value = "/enrich-ndk", produces = MediaType.APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichNdk(@RequestBody @Valid EnrichNdkRequestDto requestDto) {
        return createAndReturnResponse(requestDto.getPublications());
    }

    @PostMapping(value = "/enrich-tei", produces = MediaType.APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrichTei(@RequestBody @Valid EnrichTeiRequestDto requestDto) {
        return createAndReturnResponse(requestDto.getPublications());
    }

    private <T extends JobEventCreateDto> EnrichResponseDto createAndReturnResponse(Set<T> jobCreateDto) {
        EnrichResponseDto responseDto = new EnrichResponseDto();

        for (T createDto : jobCreateDto) {
            responseDto.getEnrichJobs().add(jobEventService.create(createDto));
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
