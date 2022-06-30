package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.OVERRIDE;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "krameriusJob", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnrichmentKrameriusJobConfigDto.class, name = "ENRICHMENT_KRAMERIUS"),
        @JsonSubTypes.Type(value = EnrichmentExternalJobConfigDto.class, name = "ENRICHMENT_EXTERNAL"),
        @JsonSubTypes.Type(value = EnrichmentNdkJobConfigDto.class, name = "ENRICHMENT_NDK"),
        @JsonSubTypes.Type(value = EnrichmentTeiJobConfigDto.class, name = "ENRICHMENT_TEI")
})
public abstract class EnrichmentJobConfigDto extends JobEventConfigCreateDto {

    @Schema(description = "If true and publications already exist, they will be overwritten. Defaults to false.")
    private boolean override = false;

    @Override
    protected void populateJobParameters() {
        jobParameters.put(OVERRIDE, override);
    }
}
