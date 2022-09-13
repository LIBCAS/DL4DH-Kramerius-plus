package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment.EnrichmentJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.ScheduledJobEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Mapper(uses = JobEventConfigMapper.class)
public interface JobPlanMapper extends DatedObjectMapper<JobPlan, JobPlanCreateDto, JobPlanDto> {

    JobEventConfigMapper configMapper = Mappers.getMapper(JobEventConfigMapper.class);

    default JobPlan fromPublicationIdAndConfigs(String publicationId, List<EnrichmentJobConfigDto> configs) {
        JobPlan jobPlan = new JobPlan();
        AtomicInteger orderCount = new AtomicInteger();

        configs.forEach(config -> {
            JobEvent jobEvent = new JobEvent();
            jobEvent.setPublicationId(publicationId);
            jobEvent.setConfig(configMapper.fromCreateDto(config));

            ScheduledJobEvent scheduledJobEvent = new ScheduledJobEvent();
            scheduledJobEvent.setJobEvent(jobEvent);
            scheduledJobEvent.setOrder(orderCount.getAndIncrement());
            scheduledJobEvent.setJobPlan(jobPlan);

            jobPlan.getScheduledJobEvents().add(scheduledJobEvent);
        });

        return jobPlan;
    }

    default Set<JobPlan> fromPublicationsToCreateDtoSet(Set<String> publicationIds, List<EnrichmentJobConfigDto> configs) {
        Set<JobPlan> result = new HashSet<>();
        publicationIds.forEach(publicationId -> {
            result.add(fromPublicationIdAndConfigs(publicationId, configs));
        });

        return result;
    }
}
