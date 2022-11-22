package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.PersistedErrorStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReportStore;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PAGE_SKIP_COUNT;

@Component
@StepScope
public class CustomSkipPolicy implements SkipPolicy, SkipListener<Page, Page> {

    @Value("#{jobParameters['" + PAGE_SKIP_COUNT + "']}")
    private Integer pageSkipTolerance = 0;

    private final StepRunReport stepRunReport;

    private final StepRunReportStore stepRunReportStore;

    private final PersistedErrorStore persistedErrorStore;

    @Autowired
    public CustomSkipPolicy(StepRunReportStore stepRunReportStore,
                            PersistedErrorStore persistedErrorStore,
                            @Value("#{jobParameters['" + JOB_EVENT_ID+ "']}") String jobEventId,
                            JobEventStore jobEventStore) {
        this.stepRunReportStore = stepRunReportStore;
        this.stepRunReport = createReport(jobEventStore, jobEventId);
        this.persistedErrorStore = persistedErrorStore;
    }

    private StepRunReport createReport(JobEventStore jobEventStore, String jobEventId) {
        JobEvent jobEvent = jobEventStore.find(jobEventId);
        StepRunReport stepRunReport = new StepRunReport();
        stepRunReport.setJobEvent(jobEvent);

        stepRunReportStore.create(stepRunReport);

        return stepRunReport;
    }

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (skipCount > pageSkipTolerance) {
            throw new SkipLimitExceededException(pageSkipTolerance, t);
        }

        return true;
    }

    @Override
    public void onSkipInRead(Throwable t) {

    }

    @Override
    public void onSkipInWrite(Page item, Throwable t) {

    }

    @Override
    public void onSkipInProcess(Page item, Throwable t) {
        stepRunReport.addError(t, item);
        stepRunReportStore.update(stepRunReport);
    }
}
