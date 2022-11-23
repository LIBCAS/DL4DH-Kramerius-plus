package cz.inqool.dl4dh.krameriusplus.service.system.job.step;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReportStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PAGE_SKIP_COUNT;

@Component
@StepScope
public class ErrorPersistingSkipPolicy extends ItemListenerSupport<Page, Page> implements SkipPolicy {

    @Value("#{jobParameters['" + PAGE_SKIP_COUNT + "']}")
    private Integer pageSkipTolerance = 0;

    private StepRunReport stepRunReport;

    private final JobEvent jobEvent;

    private final StepRunReportStore stepRunReportStore;


    @Autowired
    public ErrorPersistingSkipPolicy(StepRunReportStore stepRunReportStore,
                                     @Value("#{jobParameters['" + JOB_EVENT_ID+ "']}") String jobEventId,
                                     JobEventStore jobEventStore) {
        this.stepRunReportStore = stepRunReportStore;
        this.jobEvent = jobEventStore.find(jobEventId);
    }

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {

        stepRunReport = skipCount == 0 ? stepRunReportStore.create(stepRunReport) : stepRunReportStore.update(stepRunReport);
        // on first error skipCount == 0
        if (skipCount >= pageSkipTolerance) {
            throw new SkipLimitExceededException(pageSkipTolerance, t);
        }

        return true;
    }

    @Override
    @Transactional
    public void onProcessError(Page item, Exception t) {
        if (stepRunReport == null) {
            this.stepRunReport = new StepRunReport();
            stepRunReport.setJobEvent(jobEvent);
            stepRunReport.addError(t, item);
        } else {
            stepRunReport.addError(t, item);
        }
    }
}