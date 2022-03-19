package cz.inqool.dl4dh.krameriusplus.core.batch.job;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.MissingObjectException;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class JobService {

    private final JobExplorer jobExplorer;

    private final SimpleJobOperator jobOperator;

    private final JobMapper jobMapper;

    @Autowired
    public JobService(JobExplorer jobExplorer, SimpleJobOperator jobOperator, JobMapper jobMapper) {
        this.jobExplorer = jobExplorer;
        this.jobOperator = jobOperator;
        this.jobMapper = jobMapper;
    }

    public List<String> listJobs() {
        return jobExplorer.getJobNames();
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
