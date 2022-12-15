package cz.inqool.dl4dh.krameriusplus.api.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentRequestItemDto extends DomainObjectDto {

    private String publicationId;

    private String publicationTitle;

    private String model;

    private List<EnrichmentChainDto> enrichmentChains = new ArrayList<>();
}
