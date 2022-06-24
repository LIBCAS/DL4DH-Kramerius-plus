package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventRunDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobEventRunner {

    private JobEventService jobEventService;

    private JobEventLauncher jobEventLauncher;

    public void runJob(JobEventRunDto jobEventRunDto) {
        JobEvent jobEvent = jobEventService.findEntity(jobEventRunDto.getJobEventId());
        try {
            jobEventLauncher.run(jobEvent);
        } catch (Exception e) {
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
}
