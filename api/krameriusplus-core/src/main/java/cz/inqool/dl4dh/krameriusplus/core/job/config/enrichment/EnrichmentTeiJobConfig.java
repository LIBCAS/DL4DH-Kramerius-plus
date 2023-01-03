package cz.inqool.dl4dh.krameriusplus.core.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class EnrichmentTeiJobConfig extends EnrichmentJobConfig {

    @Override
    public KrameriusJobType getJobType() {
        return KrameriusJobType.ENRICHMENT_TEI;
    }
}
