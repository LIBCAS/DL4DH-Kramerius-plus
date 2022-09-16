package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.EnrichmentRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public QueryResults<EnrichmentRequestDto> list(String name, String owner, int page, int pageSize) {
        return service.list(name, owner, page, pageSize);
    }
}
