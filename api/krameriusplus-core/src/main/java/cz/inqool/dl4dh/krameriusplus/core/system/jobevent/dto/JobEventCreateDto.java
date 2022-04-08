package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectCreateDto;
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
}
