package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.PersistedError;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.PersistedErrorStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReportStore;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

public abstract class FaultTolerantProcessor<IN, OUT> implements ItemProcessor<IN, OUT> {

    private StepRunReportStore stepRunReportStore;

    private PersistedErrorStore persistedErrorStore;

    private JobEvent jobEvent;

    private StepRunReport stepRunReport;

    //TODO: pass through job parameters
    private Integer tolerance = 2;

    private Integer failureCount = 0;

    // TODO: how many exceptions to persist?, skip all pages if one fails? when does the job fail?
    @Override
    public OUT process(IN item) throws SkipLimitExceededException {
        try {
            return doProcess(item);
        } catch (Exception e) {
            failureCount += 1;
            if (tolerance > failureCount) {
                if (stepRunReport == null) {
                    StepRunReport stepRunReport = new StepRunReport();
                    stepRunReport.setJobEvent(jobEvent);
                    this.stepRunReport = stepRunReportStore.create(stepRunReport);
                }
                persistedErrorStore.create(toPersistedError(e, item));
                return null;
            }
            throw new SkipLimitExceededException(tolerance, e);
        }
    }

    private PersistedError toPersistedError(Exception e, IN item) {
        PersistedError persistedError = new PersistedError();
        if (item instanceof Page) {
            Page page = ((Page) item);
            persistedError.setPageId(page.getId());
            persistedError.setPageIndex(page.getIndex());
        }

        persistedError.setShortMessage(e.getMessage());
        persistedError.setStepRunReport(stepRunReport);
        return persistedError;
    }

    protected abstract OUT doProcess(IN item) throws Exception;

    @Autowired
    public void setStepRunReportStore(StepRunReportStore stepRunReportStore) {
        this.stepRunReportStore = stepRunReportStore;
    }

    @Autowired
    public void setPersistedErrorStore(PersistedErrorStore persistedErrorStore) {
        this.persistedErrorStore = persistedErrorStore;
    }

    @Autowired
    public void setJobEvent(@Value("#{jobParameters['" + JOB_EVENT_ID + "']}") String jobEventId,
                            JobEventStore jobEventStore) {
        this.jobEvent = jobEventStore.find(jobEventId);
    }
}
