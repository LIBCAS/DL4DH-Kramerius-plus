package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.EnrichmentJobConfig;
import cz.inqool.dl4dh.krameriusplus.corev2.request.Request;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kplus_enrichment_request")
public class EnrichmentRequest extends Request {

    @OneToMany
    @JoinColumn(name = "enrichment_request_id")
    private List<EnrichmentJobConfig> configs = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "enrichment_request_id")
    private List<EnrichmentRequestItem> items = new ArrayList<>();

    @OneToOne
    private KrameriusJobInstance createRequestJob;
}
