package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobEventConfig;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.JobPlanStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.ScheduledJobEvent;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class JobPlanService implements DatedService<JobPlan, JobPlanCreateDto, JobPlanDto> {

    @Getter
    private final JobPlanStore store;

    @Getter
    private final JobPlanMapper mapper;

    private final JobEventService jobEventService;

    @Autowired
    public JobPlanService(JobPlanStore store, JobPlanMapper mapper, JobEventService jobEventService) {
        this.store = store;
        this.mapper = mapper;
        this.jobEventService = jobEventService;
    }

    @Override
    @Transactional
    public JobPlan create(@NonNull JobPlan jobPlan) {
        for (ScheduledJobEvent scheduledJobEvent : jobPlan.getScheduledJobEvents()) {
            jobEventService.create(scheduledJobEvent.getJobEvent());
        }

        return store.create(jobPlan);
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
     * @param publicationId id of the publication, for which a new JobPlan should be created
     * @return created JobPlanDto
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JobPlanDto createForChild(String jobEventId, String publicationId) {
        JobEvent parentEvent = jobEventService.findEntity(jobEventId);
        notNull(parentEvent, () -> new MissingObjectException(JobEvent.class, jobEventId));

        JobPlan jobPlanBlueprint = store.findByJobEvent(jobEventId);
        notNull(jobPlanBlueprint, () -> new MissingObjectException(JobPlan.class, jobEventId));

        JobPlan childPlan = createCopy(jobPlanBlueprint, publicationId);

        return mapper.toDto(create(childPlan));
    }

    private JobPlan createCopy(JobPlan blueprint, String publicationId) {
        JobPlan copy = new JobPlan();

        for (ScheduledJobEvent scheduledJobEvent : blueprint.getScheduledJobEvents()) {
            ScheduledJobEvent scheduledJobEventCopy = new ScheduledJobEvent();
            scheduledJobEventCopy.setOrder(scheduledJobEvent.getOrder());
            scheduledJobEventCopy.setJobEvent(createCopy(scheduledJobEvent.getJobEvent(), publicationId));
            scheduledJobEventCopy.setJobPlan(copy);

            copy.getScheduledJobEvents().add(scheduledJobEventCopy);
        }

        return copy;
    }

    private JobEvent createCopy(JobEvent blueprint, String publicationId) {
        JobEvent copy = new JobEvent();
        copy.setPublicationId(publicationId);
        copy.setConfig(createConfigCopy(blueprint.getConfig()));

        return copy;
    }

    private JobEventConfig createConfigCopy(JobEventConfig config) {
        JobEventConfig newConfig = new JobEventConfig();
        newConfig.setKrameriusJob(config.getKrameriusJob());
        newConfig.setParameters(new HashMap<>(config.getParameters()));

        return newConfig;
    }
}
