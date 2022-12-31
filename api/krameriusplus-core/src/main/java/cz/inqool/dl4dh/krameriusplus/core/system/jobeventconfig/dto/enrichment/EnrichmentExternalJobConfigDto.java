package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrichmentExternalJobConfigDto extends EnrichmentJobConfigDto {

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.ENRICHMENT_EXTERNAL;
    }
}
