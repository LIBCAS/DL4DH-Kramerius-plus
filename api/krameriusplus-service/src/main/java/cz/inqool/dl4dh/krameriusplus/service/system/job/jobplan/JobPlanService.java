package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JobPlanService {

    private final JobPlanStore store;

    private final JobPlanMapper mapper;

    private final JobEventService jobEventService;

    private final JobEventMapper jobEventMapper;

    @Autowired
    public JobPlanService(JobPlanStore store, JobPlanMapper mapper, JobEventService jobEventService, JobEventMapper jobEventMapper) {
        this.store = store;
        this.mapper = mapper;
        this.jobEventService = jobEventService;
        this.jobEventMapper = jobEventMapper;
    }

    @Transactional
    public JobPlanDto create(JobPlanCreateDto planCreateDto) {
        JobPlan jobPlan = new JobPlan();

        Set<ScheduledJobEvent> scheduledJobEvents = new HashSet<>();

        int order = 0;
        for (JobEventCreateDto jobEventCreateDto : planCreateDto.getJobs()) {
            JobEvent jobEvent = jobEventMapper.fromDto(jobEventService.create(jobEventCreateDto));

            ScheduledJobEvent scheduledJobEvent = new ScheduledJobEvent();
            scheduledJobEvent.setOrder(order++);
            scheduledJobEvent.setJobEvent(jobEvent);
            scheduledJobEvent.setJobPlan(jobPlan);

            scheduledJobEvents.add(scheduledJobEvent);
        }

        jobPlan.setScheduledJobEvents(scheduledJobEvents);

        jobPlan = store.create(jobPlan);

        startExecution(jobPlan);

        return mapper.toDto(jobPlan);
    }

    private void startExecution(JobPlan jobPlan) {
        Optional<JobEvent> jobEventToRun = jobPlan.getNextToExecute();

        if (jobEventToRun.isEmpty()) {
            throw new IllegalStateException("ExecutionPlan must have at least one JobEvent to execute");
        }

        jobEventService.enqueueJob(jobEventToRun.get().getId());
    }
}
