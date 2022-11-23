package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PAGE_ERROR_TOLERANCE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ERROR_TOLERANCE;

@Getter
@Setter
public class EnrichmentExternalJobConfigDto extends EnrichmentJobConfigDto {

    private Integer publicationErrorTolerance = 0;

    private Integer pageErrorTolerance = 0;

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.ENRICHMENT_EXTERNAL;
    }

    @Override
    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = super.toJobParametersMap();
        jobParametersMap.put(PUBLICATION_ERROR_TOLERANCE, publicationErrorTolerance);
        jobParametersMap.put(PAGE_ERROR_TOLERANCE, pageErrorTolerance);

        return jobParametersMap;
    }
}
