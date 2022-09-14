package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExport;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.dto.BulkExportMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.MergeExportsJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.ScheduledJobEvent;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {JobPlanMapper.class, BulkExportMapper.class, JobEventConfigMapper.class})
public interface ExportRequestMapper extends DatedObjectMapper<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {

    JobPlanMapper jobPlanMapper = Mappers.getMapper(JobPlanMapper.class);

    JobEventConfigMapper configMapper = Mappers.getMapper(JobEventConfigMapper.class);

    @Override
    default ExportRequest fromCreateDto(ExportRequestCreateDto createDto) {
        JobPlan jobPlan = jobPlanMapper.forEachPublicationIdSameConfig(createDto.getPublicationIds(), createDto.getConfig());
        jobPlan.getScheduledJobEvents().add(createMergeJob(jobPlan));

        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setName(createDto.getName());
        exportRequest.setJobPlan(jobPlan);
        exportRequest.setBulkExport(new BulkExport());

        return exportRequest;
    }

    private ScheduledJobEvent createMergeJob(JobPlan jobPlan) {
        JobEvent mergeJob = new JobEvent();
        mergeJob.setConfig(configMapper.fromCreateDto(new MergeExportsJobConfigDto()));

        ScheduledJobEvent scheduledJobEvent = new ScheduledJobEvent();
        scheduledJobEvent.setJobPlan(jobPlan);
        scheduledJobEvent.setJobEvent(mergeJob);
        scheduledJobEvent.setOrder(Integer.MAX_VALUE);

        return scheduledJobEvent;
    }
}