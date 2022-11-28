package cz.inqool.dl4dh.krameriusplus.api.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentChainDto extends DomainObjectDto {

    private String publicationId;

    private List<KrameriusJobInstanceDto> jobs = new ArrayList<>();
}
