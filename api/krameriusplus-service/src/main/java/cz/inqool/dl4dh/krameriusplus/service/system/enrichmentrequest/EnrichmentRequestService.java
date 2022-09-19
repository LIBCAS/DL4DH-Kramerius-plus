package cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import lombok.Getter;
import lombok.NonNull;
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
    public EnrichmentRequestService(EnrichmentRequestStore store,
                                    EnrichmentRequestMapper mapper,
                                    JobPlanService jobPlanService) {
        this.store = store;
        this.mapper = mapper;
        this.jobPlanService = jobPlanService;
    }

    @Transactional
    @Override
    public EnrichmentRequestDto create(@NonNull EnrichmentRequestCreateDto createDto) {
        EnrichmentRequest entity = mapper.fromCreateDto(createDto);

        for (JobPlan jobPlan : entity.getJobPlans()) {
            entity.getJobPlans().add(jobPlanService.create(jobPlan));
        }

        return mapper.toDto(store.create(entity));
    }

    public void startExecution(EnrichmentRequestDto enrichmentRequestDto) {
        for (JobPlanDto jobPlanDto : enrichmentRequestDto.getJobPlans()) {
            jobPlanService.startExecution(jobPlanDto);
        }
    }

    public QueryResults<EnrichmentRequestDto> list(String name, String owner, int page, int pageSize) {
        QueryResults<EnrichmentRequest> results = store.list(name, owner, page, pageSize);

        return new QueryResults<>(mapper.toDtoList(results.getResults()), results.getLimit(), results.getOffset(), results.getTotal());
    }

    public EnrichmentRequestDto findByPlan(String jobPlanId) {
        return mapper.toDto(store.findByPlan(jobPlanId));
    }
}
