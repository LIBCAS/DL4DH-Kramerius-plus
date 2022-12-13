package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentFacade;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.JobParametersMapWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.CREATE_ENRICHMENT_REQUEST;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Component
public class EnrichmentRequestFacade implements EnrichmentFacade {

    private EnrichmentRequestService service;

    private KrameriusJobInstanceService jobInstanceService;

    private JobEnqueueService enqueueService;

    @Override
    @Transactional
    public EnrichmentRequestDto enrich(EnrichmentRequestCreateDto createDto) {
        EnrichmentRequest enrichmentRequest = service.getMapper().fromCreateDto(createDto);
        enrichmentRequest.getConfigs().forEach(config -> config.setEnrichmentRequest(enrichmentRequest));

        JobParametersMapWrapper jobParameters = new JobParametersMapWrapper();
        jobParameters.putString(ENRICHMENT_REQUEST_ID, enrichmentRequest.getId());

        KrameriusJobInstance createRequestJob = jobInstanceService.createJobInstance(
                CREATE_ENRICHMENT_REQUEST, jobParameters);

        enrichmentRequest.setCreateRequestJob(createRequestJob);
        EnrichmentRequestDto result = service.getMapper().toDto(service.create(enrichmentRequest));

        enqueueService.enqueue(createRequestJob);

        return result;
    }

    @Override
    public EnrichmentRequestDto find(String requestId) {
        return service.find(requestId);
    }

    @Override
    public Result<EnrichmentRequestDto> list(String name, String owner, int page, int pageSize) {
        return service.list(name, owner, page, pageSize);
    }

    @Autowired
    public void setService(EnrichmentRequestService service) {
        this.service = service;
    }

    @Autowired
    public void setJobInstanceService(KrameriusJobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }

    @Autowired
    public void setEnqueueService(JobEnqueueService enqueueService) {
        this.enqueueService = enqueueService;
    }
}
