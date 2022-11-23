package cz.inqool.dl4dh.krameriusplus.service.system.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.service.system.jobevent.dto.JobEventDetailDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ScheduledJobEventDto extends DatedObjectDto {

    @NotNull
    private Integer order;

    @NotNull
    private JobEventDetailDto jobEvent;
}
