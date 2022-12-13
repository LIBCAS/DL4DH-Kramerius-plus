package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.JobParametersMapWrapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Service
public class EnrichmentRequestService implements DatedService<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {

    @Getter
    private EnrichmentRequestStore store;

    @Getter
    private EnrichmentRequestMapper mapper;

    private KrameriusJobInstanceService jobInstanceService;

    @Transactional
    @Override
    public EnrichmentRequestDto create(EnrichmentRequestCreateDto createDto) {
        EnrichmentRequest entity = mapper.fromCreateDto(createDto);
        entity.setCreateRequestJob(createRequestJob(entity));

        return mapper.toDto(store.create(entity));
    }

    public Result<EnrichmentRequestDto> list(String name, String owner, int page, int pageSize) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    private KrameriusJobInstance createRequestJob(EnrichmentRequest request) {
        JobParametersMapWrapper jobParameters = new JobParametersMapWrapper();
        jobParameters.putString(ENRICHMENT_REQUEST_ID, request.getId());

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
