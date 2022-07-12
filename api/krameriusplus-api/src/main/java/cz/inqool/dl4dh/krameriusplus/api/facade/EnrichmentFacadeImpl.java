package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.SingleJobEnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.EnrichmentRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestSimplifiedCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentFacadeImpl implements EnrichmentFacade {

    private final EnrichmentRequestService enrichmentRequestService;

    private final JobPlanService jobPlanService;

    private final JobEventService jobEventService;

    private final JobPlanMapper jobPlanMapper;

    @Autowired
    public EnrichmentFacadeImpl(EnrichmentRequestService enrichmentRequestService, JobPlanService jobPlanService,
                                JobEventService jobEventService, JobPlanMapper jobPlanMapper) {
        this.enrichmentRequestService = enrichmentRequestService;
        this.jobPlanService = jobPlanService;
        this.jobEventService = jobEventService;
        this.jobPlanMapper = jobPlanMapper;
    }

    @Override
    public EnrichResponseDto enrich(SingleJobEnrichmentRequestDto requestDto) {
        EnrichResponseDto responseDto = new EnrichResponseDto();

        for (String publicationId : requestDto.getPublicationIds()) {
            JobEventCreateDto createDto = new JobEventCreateDto();
            createDto.setPublicationId(publicationId);
            createDto.setConfig(requestDto.getConfig());

            JobEventDto jobEventDto = jobEventService.createAndEnqueue(createDto);

            responseDto.getEnrichJobs().add(jobEventDto);
        }

        return responseDto;
    }

    @Override
    public EnrichmentRequestDto enrich(EnrichmentRequestSimplifiedCreateDto createDto) {
        EnrichmentRequestDto resultDto = enrichmentRequestService.create(createDto);

        enrichmentRequestService.startExecution(resultDto);

        return resultDto;
    }
}
