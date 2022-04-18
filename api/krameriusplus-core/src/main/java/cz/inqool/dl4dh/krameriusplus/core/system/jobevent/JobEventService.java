package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.JobEventCreateDto;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

@Service
public class JobEventService implements DatedService<JobEvent, JobEventCreateDto, JobEventDto> {

    @Getter
    private JobEventStore store;

    @Getter
    private JobEventMapper mapper;

    private Map<String, Job> jobs = new HashMap<>();

    private JmsProducer jmsProducer;

    private JobLauncher jobLauncher;

    private JobOperator jobOperator;

    private JobExplorer jobExplorer;

    @Override
    public JobEventDto create(@Valid @NonNull JobEventCreateDto createDto) {
        JobEvent jobEvent = getStore().create(getMapper().fromCreateDto(createDto));

        jmsProducer.sendMessage(jobEvent);

        return getMapper().toDto(jobEvent);
    }

    @Override
    public JobEventDto find(@NonNull String id) {
        JobEvent jobEvent = store.find(id);
        List<JobExecution> executions = new ArrayList<>();

        if (jobEvent.getInstanceId() != null) {
            JobInstance instance = jobExplorer.getJobInstance(jobEvent.getInstanceId());
            executions = jobExplorer.getJobExecutions(Objects.requireNonNull(instance));
        }

        return mapper.toDto(jobEvent, executions);
    }

    public JobEventDto runJob(String jobEventId) {
        JobEvent jobEvent = store.find(jobEventId);

        Job jobToRun = jobs.get(jobEvent.getKrameriusJob().name());

        try {
            Long newExecutionId;
            if (jobEvent.getLastExecutionId() != null) {
                newExecutionId = jobOperator.restart(jobEvent.getLastExecutionId());
            } else {
                JobExecution jobExecution = jobLauncher.run(jobToRun, jobEvent.toJobParameters());
                jobEvent.setInstanceId(jobExecution.getJobInstance().getInstanceId());
                newExecutionId = jobExecution.getId();
            }

            jobEvent.setLastExecutionId(newExecutionId);
            return mapper.toDto(store.update(jobEvent));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to run job", e);
        }
    }

    @Autowired
    public void setJobEventStore(JobEventStore jobEventStore) {
        this.store = jobEventStore;
    }

    @Autowired
    public void setMapper(JobEventMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    @Autowired
    public void setJobs(Set<Job> jobs) {
        jobs.forEach(job -> this.jobs.put(job.getName(), job));
    }

    @Autowired
    public void setJobService(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }
}
