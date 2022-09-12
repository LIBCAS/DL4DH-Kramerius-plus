package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.mapstruct.Mapper;

@Mapper(uses = JobPlanMapper.class)
public interface ExportRequestMapper extends DatedObjectMapper<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {
}
