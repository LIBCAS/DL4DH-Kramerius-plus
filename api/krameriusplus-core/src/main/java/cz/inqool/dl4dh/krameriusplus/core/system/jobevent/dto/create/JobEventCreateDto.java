package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class JobEventCreateDto extends DatedObjectCreateDto {

    private String jobName;

    private JobEventDto parent;

    @NotNull
    private String publicationId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public abstract KrameriusJob getKrameriusJob();
}
