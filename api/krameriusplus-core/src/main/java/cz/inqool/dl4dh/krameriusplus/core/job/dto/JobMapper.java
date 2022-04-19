package cz.inqool.dl4dh.krameriusplus.core.job.dto;

import org.mapstruct.Mapper;
import org.springframework.batch.core.*;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface JobMapper {

    JobExecutionDto jobExecutionToJobExecutionDto(JobExecution jobExecution);

    default Map<String, JobParameter> jobParametersToMap(JobParameters jobParameters) {
        return new HashMap<>(jobParameters.getParameters());
    }

    JobInstanceDto jobInstanceToJobInstanceDto(JobInstance jobInstance);

    StepExecutionDto stepExecutionToStepExecutionDto(StepExecution stepExecution);

    default String exitStatusToString(ExitStatus exitStatus) {
        return exitStatus.getExitCode();
    }
}
