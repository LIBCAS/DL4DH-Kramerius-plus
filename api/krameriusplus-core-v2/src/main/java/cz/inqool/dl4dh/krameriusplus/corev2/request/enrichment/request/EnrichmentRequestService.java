package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.DatedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrichmentRequestService implements DatedService<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {

    @Getter
    private EnrichmentRequestStore store;

    @Getter
    private EnrichmentRequestMapper mapper;

    public Result<EnrichmentRequestDto> list(String name, String owner, int page, int pageSize) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Autowired
    public void setStore(EnrichmentRequestStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(EnrichmentRequestMapper mapper) {
        this.mapper = mapper;
    }
}
