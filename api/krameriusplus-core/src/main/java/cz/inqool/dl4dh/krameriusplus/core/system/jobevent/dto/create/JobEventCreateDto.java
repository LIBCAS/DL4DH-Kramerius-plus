package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
public abstract class JobEventCreateDto extends DatedObjectCreateDto {

    @Schema(description = "Optional name for this job")
    private String jobName;

    @Schema(hidden = true)
    private JobEventDto parent;

    @NotNull
    @Schema(description = "UUID of the publication, that should be processed by this job.")
    private String publicationId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public abstract KrameriusJob getKrameriusJob();
}
