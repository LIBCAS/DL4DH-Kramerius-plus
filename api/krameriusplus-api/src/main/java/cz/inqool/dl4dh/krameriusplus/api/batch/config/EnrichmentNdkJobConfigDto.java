package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;

@Getter
public class EnrichmentNdkJobConfigDto extends EnrichmentJobConfigDto {

    private final KrameriusJobType jobType = KrameriusJobType.ENRICHMENT_NDK;
}
