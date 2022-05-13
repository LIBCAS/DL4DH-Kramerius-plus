package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.JobPlanResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.DownloadKStructureRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.ExecutionPlanRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.WebClientDataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan.JobPlanService;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan.dto.JobPlanCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException.ErrorCode.ALREADY_ENRICHED;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.isTrue;

@Component
public class EnrichmentFacadeImpl implements EnrichmentFacade {

    private final PublicationService publicationService;

    private final JobPlanService jobPlanService;

    private final JobEventService jobEventService;

    private final WebClientDataProvider krameriusDataProvider;

    @Autowired
    public EnrichmentFacadeImpl(PublicationService publicationService, JobPlanService jobPlanService,
                                JobEventService jobEventService, WebClientDataProvider krameriusDataProvider) {
        this.publicationService = publicationService;
        this.jobPlanService = jobPlanService;
        this.jobEventService = jobEventService;
        this.krameriusDataProvider = krameriusDataProvider;
    }

    @Override
    public EnrichResponseDto enrich(EnrichmentRequestDto requestDto) {
        if (requestDto instanceof DownloadKStructureRequestDto) {
            validate((DownloadKStructureRequestDto) requestDto);
        }

        EnrichResponseDto responseDto = new EnrichResponseDto();

        for (String publicationId : requestDto.getPublicationIds()) {
            JobEventCreateDto createDto = new JobEventCreateDto();
            createDto.setPublicationId(publicationId);
            createDto.setConfig(requestDto.getConfig());

            JobEventDto jobEventDto = jobEventService.create(createDto);
            jobEventService.enqueueJob(jobEventDto.getId());

            responseDto.getEnrichJobs().add(jobEventDto);
        }

        return responseDto;
    }

    @Override
    public JobPlanResponseDto enrichWithPlan(ExecutionPlanRequestDto requestDto) {
        JobPlanResponseDto responseDto = new JobPlanResponseDto();

        for (String publicationId : requestDto.getPublicationIds()) {
            JobPlanCreateDto planCreateDto = new JobPlanCreateDto();

            for (JobEventConfigCreateDto configCreateDto : requestDto.getConfigs()) {
                JobEventCreateDto jobEventCreateDto = new JobEventCreateDto();
                jobEventCreateDto.setPublicationId(publicationId);
                jobEventCreateDto.setConfig(configCreateDto);

                planCreateDto.getJobs().add(jobEventCreateDto);
            }

            responseDto.getJobPlans().add(jobPlanService.create(planCreateDto));
        }

        return responseDto;
    }

    private void validate(DownloadKStructureRequestDto requestDto) {
        List<String> alreadyEnrichedPublications = collectAlreadyEnriched(requestDto.getPublicationIds());

        isTrue(requestDto.isOverride() || alreadyEnrichedPublications.isEmpty(),
                () -> new SchedulingException(ALREADY_ENRICHED,
                        "Publications " + alreadyEnrichedPublications + " are already enriched, to override, repeat request with request parameter" +
                                " 'override=true'"));

        validateIdentifiers(requestDto.getPublicationIds());
    }

    private List<String> collectAlreadyEnriched(Set<String> publicationIds) {
        List<String> alreadyEnrichedPublications = new ArrayList<>();

        publicationIds.forEach(publicationId -> {
            if (publicationService.exists(publicationId)) {
                alreadyEnrichedPublications.add(publicationId);
            }
        });

        return alreadyEnrichedPublications;
    }

    private void validateIdentifiers(Set<String> publicationIds) {
        for (String publicationId : publicationIds) {
            krameriusDataProvider.getDigitalObject(publicationId);
        }
    }
}
