package cz.inqool.dl4dh.krameriusplus.api.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceGridDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentChainDto extends DomainObjectDto {

    private String publicationId;

    private String publicationTitle;

    private PublicationModel model;

    private List<KrameriusJobInstanceGridDto> jobs = new ArrayList<>();
}
