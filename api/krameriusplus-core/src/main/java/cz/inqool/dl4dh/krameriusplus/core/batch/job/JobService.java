package cz.inqool.dl4dh.krameriusplus.core.batch.job;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.MissingObjectException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
@Slf4j
public class JobService {

    private final JobExplorer jobExplorer;

    private final JobOperator jobOperator;

    private final JobLauncher jobLauncher;

    private final JobMapper jobMapper;

    @Autowired
    public JobService(JobExplorer jobExplorer, JobOperator jobOperator, JobLauncher jobLauncher, JobMapper jobMapper) {
        this.jobExplorer = jobExplorer;
        this.jobOperator = jobOperator;
        this.jobLauncher = jobLauncher;
        this.jobMapper = jobMapper;
    }

    public List<String> listJobs() {
        return jobExplorer.getJobNames();
    }

    public void runJob(Job job, @NonNull JobParameters jobParameters) {
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException |
                JobRestartException |
                JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            log.error("Error when running job", e);
        }
    }

    public void restartJob(Long executionId) {
        try {
            jobOperator.restart(executionId);
        } catch (JobInstanceAlreadyCompleteException |
                NoSuchJobExecutionException |
                NoSuchJobException |
                JobRestartException |
                JobParametersInvalidException e) {
            log.error("Failed to restart job", e);
        }
    }

    public List<JobInstanceDto> listJobInstances(String jobName) {
        return jobExplorer.getJobInstances(jobName, 0, Integer.MAX_VALUE)
                .stream()
                .map(jobMapper::jobInstanceToJobInstanceDto)
                .collect(Collectors.toList());
    }

    public List<JobExecutionDto> listJobExecutions(Long instanceId) {
        JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
        notNull(jobInstance, () -> new MissingObjectException(JobInstance.class, String.valueOf(instanceId)));

        return jobExplorer.getJobExecutions(jobInstance)
                .stream()
                .map(jobMapper::jobExecutionToJobExecutionDto)
                .collect(Collectors.toList());
    }
}
