package cz.inqool.dl4dh.krameriusplus.api.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan.dto.JobPlanDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JobPlanResponseDto {

    private List<JobPlanDto> jobPlans = new ArrayList<>();
}
