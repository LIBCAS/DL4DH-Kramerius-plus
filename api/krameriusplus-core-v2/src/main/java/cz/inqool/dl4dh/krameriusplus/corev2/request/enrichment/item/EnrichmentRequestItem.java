package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kplus_enrichment_request_item")
public class EnrichmentRequestItem extends DomainObject {

    @NotNull
    private String publicationId;

    @OneToMany
    private List<EnrichmentChain> enrichmentChains = new ArrayList<>();

    private Long order;
}
