package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigDto;
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
    private JobEventConfigDto config;
}
