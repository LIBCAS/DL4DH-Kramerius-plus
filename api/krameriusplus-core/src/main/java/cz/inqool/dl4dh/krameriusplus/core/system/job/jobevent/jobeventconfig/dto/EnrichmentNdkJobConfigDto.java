package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnrichmentNdkJobConfigDto extends EnrichJobConfigDto {

    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_NDK;

    @Override
    public Map<String, Object> getJobParameters() {
        return super.createJobParameters();
    }
}