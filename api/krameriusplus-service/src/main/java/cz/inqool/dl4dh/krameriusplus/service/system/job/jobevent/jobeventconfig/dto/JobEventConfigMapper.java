package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.JobEventConfig;
import org.mapstruct.Mapper;

@Mapper
public interface JobEventConfigMapper {

    default JobEventConfig fromCreateDto(JobEventConfigCreateDto createDto) {
        JobEventConfig jobEventConfig = new JobEventConfig();
        jobEventConfig.setKrameriusJob(createDto.getKrameriusJob());
        jobEventConfig.setParameters(createDto.getJobParameters());

        return jobEventConfig;
    }
}
