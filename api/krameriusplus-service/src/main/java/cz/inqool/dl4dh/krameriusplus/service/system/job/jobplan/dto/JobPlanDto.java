package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class JobPlanDto extends DatedObjectDto {

    private Set<ScheduledJobEventDto> scheduledJobEvents = new HashSet<>();

}
