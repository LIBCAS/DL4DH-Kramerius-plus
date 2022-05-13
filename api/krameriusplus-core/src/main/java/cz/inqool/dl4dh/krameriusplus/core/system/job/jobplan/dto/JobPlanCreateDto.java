package cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JobPlanCreateDto extends DatedObjectCreateDto {

    @NotEmpty
    private List<JobEventCreateDto> jobs = new ArrayList<>();
}
