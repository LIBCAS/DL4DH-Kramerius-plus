package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.OVERRIDE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PAGE_SKIP_COUNT;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_SKIP_COUNT;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "krameriusJob", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnrichmentKrameriusJobConfigDto.class, name = "ENRICHMENT_KRAMERIUS"),
        @JsonSubTypes.Type(value = EnrichmentExternalJobConfigDto.class, name = "ENRICHMENT_EXTERNAL"),
        @JsonSubTypes.Type(value = EnrichmentNdkJobConfigDto.class, name = "ENRICHMENT_NDK"),
        @JsonSubTypes.Type(value = EnrichmentTeiJobConfigDto.class, name = "ENRICHMENT_TEI")
})
public abstract class EnrichmentJobConfigDto extends JobEventConfigDto {

    @Schema(description = "If true and publications already exist, they will be overwritten. Defaults to false.")
    private boolean override = false;

    private Integer publicationSkipCount = Integer.MAX_VALUE;

    private Integer pageSkipCount = Integer.MAX_VALUE;

    @Override
    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = new HashMap<>();
        jobParametersMap.put(OVERRIDE, override);
        jobParametersMap.put(PAGE_SKIP_COUNT, pageSkipCount);
        jobParametersMap.put(PUBLICATION_SKIP_COUNT, publicationSkipCount);

        return jobParametersMap;
    }
}
