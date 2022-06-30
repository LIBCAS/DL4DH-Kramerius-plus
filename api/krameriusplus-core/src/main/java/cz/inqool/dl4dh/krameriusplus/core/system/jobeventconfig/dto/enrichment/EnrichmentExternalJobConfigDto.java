package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoOption;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.MISSING_ALTO_STRATEGY;

@Getter
@Setter
public class EnrichmentExternalJobConfigDto extends EnrichmentJobConfigDto {

    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_EXTERNAL;

    private MissingAltoOption missingAltoOption = MissingAltoOption.FAIL_IF_ALL_MISS;

    @Override
    protected void populateJobParameters() {
        super.populateJobParameters();
        jobParameters.put(MISSING_ALTO_STRATEGY, missingAltoOption);
    }
}
