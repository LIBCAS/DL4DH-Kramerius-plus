package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobEventConfig;
import org.mapstruct.Mapper;

@Mapper
public interface JobEventConfigMapper {

    default JobEventConfig fromCreateDto(JobEventConfigDto createDto) {
        JobEventConfig jobEventConfig = new JobEventConfig();
        jobEventConfig.setKrameriusJob(createDto.getKrameriusJob());
        jobEventConfig.setParameters(createDto.toJobParametersMap());

        return jobEventConfig;
    }
}
