package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnrichmentExternalJobConfigDto extends EnrichJobConfigDto {

    @Schema(allowableValues = {"ENRICHMENT_EXTERNAL"})
    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_EXTERNAL;

    @Override
    public Map<String, Object> getJobParameters() {
        return super.createJobParameters();
    }
}
