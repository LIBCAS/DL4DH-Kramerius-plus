package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentFacade;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.jms.JobEnqueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EnrichmentRequestFacade implements EnrichmentFacade {

    private EnrichmentRequestService service;

    private JobEnqueueService enqueueService;

    @Override
    public EnrichmentRequestDto enrich(EnrichmentRequestCreateDto createDto) {
        EnrichmentRequestDto enrichmentRequest = service.create(createDto);

        enqueueService.enqueue(enrichmentRequest.getCreateRequestJob());

        return enrichmentRequest;
    }

    @Override
    @Transactional(readOnly = true) // Important for fetching LOB values (StepError::stackTrace)
    public EnrichmentRequestDto find(String requestId) {
        return service.find(requestId);
    }

    @Override
    public Result<EnrichmentRequestDto> list(String publicationId, String name, String owner, int page, int pageSize) {
        return service.list(publicationId, name, owner, page, pageSize);
    }

    @Autowired
    public void setService(EnrichmentRequestService service) {
        this.service = service;
    }

    @Autowired
    public void setEnqueueService(JobEnqueueService enqueueService) {
        this.enqueueService = enqueueService;
    }
}
