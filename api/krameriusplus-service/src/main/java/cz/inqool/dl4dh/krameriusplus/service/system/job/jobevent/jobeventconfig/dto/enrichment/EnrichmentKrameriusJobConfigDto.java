package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnrichmentKrameriusJobConfigDto extends EnrichmentJobConfigDto {

    @Schema(allowableValues = {"ENRICHMENT_KRAMERIUS"})
    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_KRAMERIUS;

    @Override
    public Map<String, Object> getJobParameters() {
        return super.createJobParameters();
    }
}
