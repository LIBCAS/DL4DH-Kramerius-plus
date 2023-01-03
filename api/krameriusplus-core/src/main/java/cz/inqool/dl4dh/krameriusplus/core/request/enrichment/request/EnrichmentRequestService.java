package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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

    public Result<EnrichmentRequestDto> list(String name, String owner, int page, int pageSize) {
        Result<EnrichmentRequest> results = store.findByNameAndOwner(name, owner, page, pageSize);
        List<EnrichmentRequestDto> dtos = results.getItems().stream().map(mapper::toDto).collect(Collectors.toList());

        return new Result<>(pageSize, page, results.getTotal(), dtos);
    }

    private KrameriusJobInstance createRequestJob(EnrichmentRequest request) {
        JobParametersMapWrapper jobParameters = new JobParametersMapWrapper();
        jobParameters.putString(JobParameterKey.ENRICHMENT_REQUEST_ID, request.getId());

        return jobInstanceService.createJobInstance(
                KrameriusJobType.CREATE_ENRICHMENT_REQUEST, jobParameters);
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
