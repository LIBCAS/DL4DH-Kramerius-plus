package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.config.enrichment.EnrichmentJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.request.Request;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item.EnrichmentRequestItem;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrichment_request_id")
    private List<EnrichmentJobConfig> configs = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "enrichment_request_id")
    private List<EnrichmentRequestItem> items = new ArrayList<>();

    @OneToOne
    private KrameriusJobInstance createRequestJob;

    public void addConfig(EnrichmentJobConfig jobConfig) {
        configs.add(jobConfig);
        jobConfig.setEnrichmentRequest(this);
    }
}
