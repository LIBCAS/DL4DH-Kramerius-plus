package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.JobPlanResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.JobPlanRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentFacadeImpl implements EnrichmentFacade {

    private final JobPlanService jobPlanService;

    private final JobEventService jobEventService;

    private final JobPlanMapper jobPlanMapper;

    @Autowired
    public EnrichmentFacadeImpl(JobPlanService jobPlanService, JobEventService jobEventService, JobPlanMapper jobPlanMapper) {
        this.jobPlanService = jobPlanService;
        this.jobEventService = jobEventService;
        this.jobPlanMapper = jobPlanMapper;
    }

    @Override
    public EnrichResponseDto enrich(EnrichmentRequestDto requestDto) {
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
    public JobPlanResponseDto enrichWithPlan(JobPlanRequestDto requestDto) {
        JobPlanResponseDto responseDto = new JobPlanResponseDto();

        requestDto.getPublicationIds().forEach(publicationId -> {
            JobPlanCreateDto planCreateDto = jobPlanMapper.toCreateDto(publicationId, requestDto.getName(), requestDto.getConfigs());
            responseDto.getJobPlans().add(jobPlanService.create(planCreateDto));
        });

        return responseDto;
    }
}
