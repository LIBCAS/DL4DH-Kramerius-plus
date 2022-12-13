package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.ENRICHMENT_EXTERNAL;

@Getter
public class EnrichmentExternalJobConfigDto extends EnrichmentJobConfigDto {

    private final KrameriusJobType jobType = ENRICHMENT_EXTERNAL;
}
