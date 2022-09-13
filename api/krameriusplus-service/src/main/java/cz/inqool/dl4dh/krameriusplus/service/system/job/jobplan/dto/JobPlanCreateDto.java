package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JobPlanCreateDto extends DatedObjectCreateDto {

    private String name;

    @NotEmpty
    private List<ScheduledJobEventDto> scheduledJobEvents = new ArrayList<>();
}
