package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.corev2.request.PublicationModel;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @NotNull
    private String publicationTitle;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PublicationModel model;

    @OneToMany
    @JoinColumn(name = "request_item_id")
    private List<EnrichmentChain> enrichmentChains = new ArrayList<>();

    @Column(name = "item_order")
    @NotNull
    private Long order;

    @ManyToOne
    private EnrichmentRequest enrichmentRequest;
}
