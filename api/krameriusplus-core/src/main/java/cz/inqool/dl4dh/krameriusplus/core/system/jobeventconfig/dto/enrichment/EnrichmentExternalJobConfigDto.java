package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoOption;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.MISSING_ALTO_STRATEGY;

@Getter
@Setter
public class EnrichmentExternalJobConfigDto extends EnrichmentJobConfigDto {

    private MissingAltoOption missingAltoOption = MissingAltoOption.FAIL_IF_ALL_MISS;

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.ENRICHMENT_EXTERNAL;
    }

    @Override
    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = super.toJobParametersMap();
        jobParametersMap.put(MISSING_ALTO_STRATEGY, missingAltoOption);

        return jobParametersMap;
    }
}
