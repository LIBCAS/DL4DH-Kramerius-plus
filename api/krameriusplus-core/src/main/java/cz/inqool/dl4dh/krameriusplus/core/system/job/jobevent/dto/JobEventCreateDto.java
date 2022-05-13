package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class JobEventCreateDto extends DatedObjectCreateDto {

    private String jobName;

    private JobEventDto parent;

    @NotNull
    private String publicationId;

    @NotNull
    private JobEventConfigCreateDto config;
}
