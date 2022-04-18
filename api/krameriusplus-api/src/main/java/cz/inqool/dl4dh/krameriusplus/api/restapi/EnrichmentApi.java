package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.PublicationContainerDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.WebClientDataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.DownloadKStructureCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException.ErrorCode.ALREADY_ENRICHED;

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

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public EnrichResponseDto enrich(@RequestBody PublicationContainerDto publicationsDto,
                                    @RequestParam(value = "override", defaultValue = "false") boolean override) {
        List<String> alreadyEnrichedPublications = new ArrayList<>();

        publicationsDto.getPublications().forEach(pubId -> {
            if (publicationService.exists(pubId)) {
                alreadyEnrichedPublications.add(pubId);
            }
        });

        if (!alreadyEnrichedPublications.isEmpty() && !override) {
            throw new SchedulingException(ALREADY_ENRICHED,
                    "Publications " + alreadyEnrichedPublications + " are already enriched, to override, repeat request with request parameter" +
                            " 'override=true'");
        }

        validateIdentifiers(publicationsDto.getPublications());

        EnrichResponseDto responseDto = new EnrichResponseDto();

        for (String publicationId : publicationsDto.getPublications()) {
            DownloadKStructureCreateDto createDto = new DownloadKStructureCreateDto();
            createDto.setPublicationId(publicationId);
            responseDto.getEnrichJobs().add(jobEventService.create(createDto));
        }

        return responseDto;
    }

    private void validateIdentifiers(Set<String> publications) {
        for (String publicationId : publications) {
            krameriusDataProvider.getDigitalObject(publicationId);
        }
    }
}
