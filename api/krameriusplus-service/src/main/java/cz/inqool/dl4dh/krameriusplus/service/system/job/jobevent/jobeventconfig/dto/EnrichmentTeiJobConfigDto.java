package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnrichmentTeiJobConfigDto extends EnrichJobConfigDto {

    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_TEI;

    @Override
    public Map<String, Object> getJobParameters() {
        return super.createJobParameters();
    }
}
