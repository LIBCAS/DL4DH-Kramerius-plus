package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "krameriusJob")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnrichmentKrameriusJobConfigDto.class, name = "ENRICHMENT_KRAMERIUS"),
        @JsonSubTypes.Type(value = EnrichmentExternalJobConfigDto.class, name = "ENRICHMENT_EXTERNAL"),
        @JsonSubTypes.Type(value = EnrichmentNdkJobConfigDto.class, name = "ENRICHMENT_NDK"),
        @JsonSubTypes.Type(value = EnrichmentTeiJobConfigDto.class, name = "ENRICHMENT_TEI")
})
@Schema(description = "List of Configs that should be used for every publication.", discriminatorProperty = "krameriusJob")
public abstract class JobEventConfigCreateDto {

    @Schema(hidden = true)
    public abstract KrameriusJob getKrameriusJob();

    @Schema(hidden = true)
    public abstract Map<String, Object> getJobParameters();
}
