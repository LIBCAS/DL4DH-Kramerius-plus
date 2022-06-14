package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
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
