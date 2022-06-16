package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnrichmentNdkJobConfigDto extends EnrichmentJobConfigDto {

    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_NDK;

    @Override
    public Map<String, Object> getJobParameters() {
        return super.createJobParameters();
    }
}
