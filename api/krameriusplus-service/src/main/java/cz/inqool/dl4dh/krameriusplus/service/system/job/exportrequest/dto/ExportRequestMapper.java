package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {JobPlanMapper.class, BulkExportMapper.class})
public interface ExportRequestMapper extends DatedObjectMapper<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {

    BulkExportMapper bulkExportMapper = Mappers.getMapper(BulkExportMapper.class);

    JobPlanMapper jobPlanMapper = Mappers.getMapper(JobPlanMapper.class);

    @Override
    default ExportRequest fromCreateDto(ExportRequestCreateDto createDto) {
        ExportRequest exportRequest = new ExportRequest();

        exportRequest.setJobPlan(jobPlanMapper.fromDto(createDto.getJobPlanDto()));

        return exportRequest;
    }

    @Override
    default ExportRequestDto toDto(ExportRequest exportRequest) {
        ExportRequestDto exportRequestDto = new ExportRequestDto();

        exportRequestDto.setId(exportRequest.getId());
        exportRequestDto.setCreated(exportRequest.getCreated());
        exportRequestDto.setUpdated(exportRequest.getUpdated());
        exportRequestDto.setDeleted(exportRequest.getDeleted());
        exportRequestDto.setBulkExportDto(bulkExportMapper.toDto(exportRequest.getBulkExport()));
        exportRequestDto.setJobPlanDto(jobPlanMapper.toDto(exportRequest.getJobPlan()));
        exportRequestDto.setExportSet(exportRequest.getExports());
        exportRequestDto.setOwner(exportRequest.getOwner());

        return exportRequestDto;
    }
}
