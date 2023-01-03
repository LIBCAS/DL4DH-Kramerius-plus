package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.*;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "jobType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = ENRICHMENT_EXTERNAL, value = EnrichmentExternalJobConfigDto.class),
        @JsonSubTypes.Type(name = ENRICHMENT_NDK, value = EnrichmentNdkJobConfigDto.class),
        @JsonSubTypes.Type(name = ENRICHMENT_TEI, value = EnrichmentTeiJobConfigDto.class)
})
public abstract class EnrichmentJobConfigDto extends JobConfigDto {

    private boolean override = false;

    private Integer pageErrorTolerance = 0;
}
