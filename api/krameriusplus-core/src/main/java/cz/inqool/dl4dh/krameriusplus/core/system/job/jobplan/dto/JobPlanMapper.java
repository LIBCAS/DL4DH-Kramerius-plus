package cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan.JobPlan;
import org.mapstruct.Mapper;

@Mapper
public interface JobPlanMapper extends DatedObjectMapper<JobPlan, JobPlanCreateDto, JobPlanDto> {
}
