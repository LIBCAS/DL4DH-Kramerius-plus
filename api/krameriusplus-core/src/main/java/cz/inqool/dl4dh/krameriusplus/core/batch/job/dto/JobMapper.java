package cz.inqool.dl4dh.krameriusplus.core.batch.job.dto;

import org.mapstruct.Mapper;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;

@Mapper
public interface JobMapper {

    JobExecutionDto jobExecutionToJobExecutionDto(JobExecution jobExecution);

    JobInstanceDto jobInstanceToJobInstanceDto(JobInstance jobInstance);

    StepExecutionDto stepExecutionToStepExecutionDto(StepExecution stepExecution);

    default String exitStatusToString(ExitStatus exitStatus) {
        return exitStatus.getExitCode();
    }
}
