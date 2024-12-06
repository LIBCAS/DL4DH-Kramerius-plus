package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.RequestState;
import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item.EnrichmentRequestItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EnrichmentRequestService implements DatedService<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {

    @Getter
    private EnrichmentRequestStore store;

    @Getter
    private EnrichmentRequestMapper mapper;

    private KrameriusJobInstanceService jobInstanceService;

    @Transactional
    @Override
    public EnrichmentRequestDto create(@NonNull EnrichmentRequestCreateDto createDto) {
        EnrichmentRequest entity = mapper.fromCreateDto(createDto);
        entity.setCreateRequestJob(createRequestJob(entity));

        return mapper.toDto(store.save(entity));
    }

    @Transactional(readOnly = true)
    public Result<EnrichmentRequestDto> list(String publicationId, String name, String owner, String state, int page, int pageSize) {
        Result<EnrichmentRequest> results = store.findByFilter(publicationId, name, owner, state, page, pageSize);
        List<EnrichmentRequestDto> dtos = results.getItems().stream().map(mapper::toDto).collect(Collectors.toList());

        return new Result<>(page, pageSize, results.getTotal(), dtos);
    }

    private KrameriusJobInstance createRequestJob(EnrichmentRequest request) {
        JobParametersMapWrapper jobParameters = new JobParametersMapWrapper();
        jobParameters.putString(JobParameterKey.ENRICHMENT_REQUEST_ID, request.getId());

        return jobInstanceService.createJobInstance(
                KrameriusJobType.CREATE_ENRICHMENT_REQUEST, jobParameters);
    }

    @Transactional
    public EnrichmentRequestDto cancel(String id) {
        EnrichmentRequest entity = findEntity(id);

        validateCancelConditions(entity);

        List<KrameriusJobInstance> jobsToCancel = new ArrayList<>();

        log.info("Cancelling enrichment request with id: " + id);
        if (!entity.getCreateRequestJob().getExecutionStatus().isFinished()) {
            log.info("Cancelling create request Job for enrichment request id: " + id);
            jobsToCancel.add(entity.getCreateRequestJob());
        } else {
            log.info("Cancelling all enrichment jobs for request id: " + id);
            jobsToCancel.addAll(collectJobsFromRequest(entity));
        }

        jobInstanceService.cancelMultipleJobs(jobsToCancel);
        entity.setState(RequestState.CANCELLED);
        log.info("Request cancelled, owned Jobs stopping");
        return mapper.toDto(store.save(entity));
    }

    private List<KrameriusJobInstance> collectJobsFromRequest(EnrichmentRequest entity) {
        List<KrameriusJobInstance> jobsInRequest = new ArrayList<>();
        for (EnrichmentRequestItem item : entity.getItems()) {
            jobsInRequest.addAll(item.getEnrichmentChains().stream()
                    .flatMap(enrichmentChain -> enrichmentChain.getJobs().values().stream())
                    .collect(Collectors.toList()));
        }
        return jobsInRequest;
    }

    private void validateCancelConditions(EnrichmentRequest entity) {
        String id = entity.getId();
        if (entity.getState().isComplete()) {
            throw new EnrichingException("Enrichment request with id: " + id + " has status: "
                    + entity.getState() + " and is not cancellable",
                    EnrichingException.ErrorCode.REQUEST_FINISHED);
        }

        if (entity.getCreateRequestJob() == null) {
            throw new IllegalStateException("EnrichmentRequest with id: " + id + " has no create request job");
        }
    }

    @Autowired
    public void setStore(EnrichmentRequestStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(EnrichmentRequestMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJobInstanceService(KrameriusJobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }
}
