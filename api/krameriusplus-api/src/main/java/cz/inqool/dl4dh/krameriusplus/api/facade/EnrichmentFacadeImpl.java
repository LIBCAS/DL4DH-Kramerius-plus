package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.EnrichmentRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnrichmentFacadeImpl implements EnrichmentFacade {

    private final EnrichmentRequestService service;

    @Autowired
    public EnrichmentFacadeImpl(EnrichmentRequestService service) {
        this.service = service;
    }

    @Override
    public EnrichmentRequestDto enrich(EnrichmentRequestCreateDto createDto) {
        EnrichmentRequestDto resultDto = service.create(createDto);

        service.startExecution(resultDto);

        return resultDto;
    }

    @Override
    public EnrichmentRequestDto find(String enrichmentRequestId) {
        return service.find(enrichmentRequestId);
    }

    @Override
    public List<EnrichmentRequestDto> list() {
        return service.listAll();
    }
}
