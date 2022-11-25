package cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.jobevent.JobEventService;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
@StepScope
public class ErrorPersistingSkipListener implements SkipListener<Page, Page> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Value("#{jobParameters['" + JOB_EVENT_ID + "']}")
    private String jobEventId;

    private final JobEventService jobEventService;

    @Autowired
    public ErrorPersistingSkipListener(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }

    @Override
    public void onSkipInRead(Throwable t) {
        persistError(t);
    }

    @Override
    public void onSkipInWrite(Page item, Throwable t) {
        persistError(t);
    }

    @Override
    public void onSkipInProcess(Page item, Throwable t) {
        persistError(t);
    }

    private void persistError(Throwable throwable) {
        jobEventService.saveError(jobEventId, stepExecution.getId(), throwable);
    }
}
