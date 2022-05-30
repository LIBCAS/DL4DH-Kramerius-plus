package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;
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
