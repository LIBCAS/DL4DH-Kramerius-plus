package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrichmentTeiJobConfigDto extends EnrichmentJobConfigDto {

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.ENRICHMENT_TEI;
    }
}
