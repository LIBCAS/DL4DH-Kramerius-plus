package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestSimplifiedCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrichmentRequestService implements DatedService<
        EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {

    @Getter
    private final EnrichmentRequestStore store;

    @Getter
    private final EnrichmentRequestMapper mapper;

    private final JobPlanService jobPlanService;

    @Autowired
    public EnrichmentRequestService(EnrichmentRequestStore store, EnrichmentRequestMapper mapper,
                                    JobPlanService jobPlanService) {
        this.store = store;
        this.mapper = mapper;
        this.jobPlanService = jobPlanService;
    }

    @Transactional
    public EnrichmentRequestDto create(EnrichmentRequestSimplifiedCreateDto simplifiedCreateDto) {
        EnrichmentRequestCreateDto createDto = mapper.fromSimplifiedCreateDto(simplifiedCreateDto);

        EnrichmentRequest entity = new EnrichmentRequest();
        entity.setName(createDto.getName());

        for (JobPlanCreateDto jobPlanCreateDto : createDto.getJobPlans()) {
            jobPlanCreateDto.setEnrichmentRequest(entity);
            entity.getJobPlans().add(jobPlanService.create(jobPlanCreateDto));
        }

        return mapper.toDto(store.create(entity));
    }

    public void startExecution(EnrichmentRequestDto enrichmentRequestDto) {
        for (JobPlanDto jobPlanDto : enrichmentRequestDto.getJobPlans()) {
            jobPlanService.startExecution(jobPlanDto);
        }
    }
}
