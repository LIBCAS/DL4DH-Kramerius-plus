package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentFacade;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.CREATE_ENRICHMENT_REQUEST;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Component
public class EnrichmentRequestFacade implements EnrichmentFacade {

    private EnrichmentRequestService service;

    private KrameriusJobInstanceService jobInstanceService;

    private JobEnqueueService enqueueService;

    @Override
    public EnrichmentRequestDto enrich(EnrichmentRequestCreateDto createDto) {
        EnrichmentRequestDto requestDto = service.create(createDto);

        KrameriusJobInstance createRequestJob = jobInstanceService.createJob(
                CREATE_ENRICHMENT_REQUEST, Map.of(ENRICHMENT_REQUEST_ID, requestDto.getId()));

        enqueueService.enqueue(createRequestJob);

        return requestDto;
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
