package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventRunDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Component for running jobs
 */
@Component
@Slf4j
public class JobEventRunner {

    private JobEventService jobEventService;

    private JobEventLauncher jobEventLauncher;

    private JobEventMapper jobEventMapper;

    /**
     * Runs a given job asynchronously.
     * @param jobEventRunDto DTO with ID of the job to run
     */
    public void runJob(JobEventRunDto jobEventRunDto) {
        JobEvent jobEvent = jobEventService.findEntity(jobEventRunDto.getJobEventId());
        try {
            log.debug("running job: " + jobEvent.getJobName());
            jobEventLauncher.run(jobEvent);
        } catch (Exception e) {
            JobEventDto jobEventDto = jobEventMapper.toDto(jobEvent);
            jobEventDto.getDetails().setRunErrorMessage(e.getMessage());
            jobEventDto.getDetails().setRunErrorStacktrace(Arrays.stream(e.getStackTrace()).sequential()
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining()));
            jobEventService.update(jobEventDto);
            throw new IllegalStateException("Failed to run job", e);
        }
    }

    @Autowired
    public void setJobEventLauncher(JobEventLauncher jobEventLauncher) {
        this.jobEventLauncher = jobEventLauncher;
    }

    @Autowired
    public void setJobEventService(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }

    @Autowired
    public void setJobEventMapper(JobEventMapper jobEventMapper) {
        this.jobEventMapper = jobEventMapper;
    }
}
