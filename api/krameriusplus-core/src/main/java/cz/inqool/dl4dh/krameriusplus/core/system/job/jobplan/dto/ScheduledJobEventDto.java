package cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ScheduledJobEventDto extends DatedObjectDto {

    @NotNull
    private Integer order;

    @NotNull
    private JobEventDto jobEvent;
}
