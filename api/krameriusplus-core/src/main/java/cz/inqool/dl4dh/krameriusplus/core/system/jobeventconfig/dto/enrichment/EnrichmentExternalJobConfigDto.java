package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnrichmentExternalJobConfigDto extends EnrichmentJobConfigDto {

    @Schema(allowableValues = {"ENRICHMENT_EXTERNAL"})
    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_EXTERNAL;

    @Override
    public Map<String, Object> getJobParameters() {
        return super.createJobParameters();
    }
}
