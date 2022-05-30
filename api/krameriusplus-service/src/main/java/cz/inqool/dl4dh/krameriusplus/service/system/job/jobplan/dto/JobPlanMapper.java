package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import org.mapstruct.Mapper;

@Mapper
public interface JobPlanMapper extends DatedObjectMapper<JobPlan, JobPlanCreateDto, JobPlanDto> {
}
