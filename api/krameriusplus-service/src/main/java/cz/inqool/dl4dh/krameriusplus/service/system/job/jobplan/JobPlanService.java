package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobEventConfig;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

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

        int order = 0;
        for (JobEventCreateDto jobEventCreateDto : planCreateDto.getJobs()) {
            JobEvent jobEvent = jobEventMapper.fromDto(jobEventService.create(jobEventCreateDto));

            ScheduledJobEvent scheduledJobEvent = new ScheduledJobEvent();
            scheduledJobEvent.setOrder(order++);
            scheduledJobEvent.setJobEvent(jobEvent);
            scheduledJobEvent.setJobPlan(jobPlan);

            jobPlan.getScheduledJobEvents().add(scheduledJobEvent);
        }

        return mapper.toDto(store.create(jobPlan));
    }

    public void startExecution(JobPlanDto jobPlanDto) {
        JobPlan jobPlan = mapper.fromDto(jobPlanDto);
        Optional<JobEvent> jobEventToRun = jobPlan.getNextToExecute();

        if (jobEventToRun.isEmpty()) {
            throw new IllegalStateException("ExecutionPlan must have at least one JobEvent to execute");
        }

        jobEventService.enqueueJob(jobEventToRun.get());
    }

    /**
     * Creates a new JobPlan with same configs as the JobPlan, which contains the given JobEvent
     * @param jobEventId id of the JobEvent, of which JobPlan is used as a blueprint
     * @param childId id of the publication, for which a new JobPlan should be created
     * @return created JobPlanDto
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JobPlanDto createForChild(String jobEventId, String childId) {
        JobEvent parentEvent = jobEventService.findEntity(jobEventId);
        notNull(parentEvent, () -> new MissingObjectException(JobEvent.class, jobEventId));

        JobPlan jobPlanBlueprint = store.findByJobEvent(jobEventId);
        JobPlan newPlan = new JobPlan();

        for (ScheduledJobEvent scheduledJobEvent : jobPlanBlueprint.getScheduledJobEvents()) {
            JobEvent newJobEvent = new JobEvent();
            newJobEvent.setPublicationId(childId);
            newJobEvent.setParent(parentEvent);
            newJobEvent.setConfig(createConfigCopy(scheduledJobEvent.getJobEvent().getConfig()));

            jobEventService.create(newJobEvent);

            ScheduledJobEvent newScheduledJobEvent = new ScheduledJobEvent();
            newScheduledJobEvent.setJobEvent(newJobEvent);
            newScheduledJobEvent.setJobPlan(newPlan);
            newScheduledJobEvent.setOrder(scheduledJobEvent.getOrder());

            newPlan.getScheduledJobEvents().add(newScheduledJobEvent);
        }

        return mapper.toDto(store.create(newPlan));
    }

    private JobEventConfig createConfigCopy(JobEventConfig config) {
        JobEventConfig newConfig = new JobEventConfig();
        newConfig.setKrameriusJob(config.getKrameriusJob());
        newConfig.setParameters(new HashMap<>(config.getParameters()));

        return newConfig;
    }
}
