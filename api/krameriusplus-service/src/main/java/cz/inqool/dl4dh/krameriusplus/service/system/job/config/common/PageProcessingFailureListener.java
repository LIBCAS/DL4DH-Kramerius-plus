package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.PersistedError;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.PersistedErrorStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReportStore;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.EXCEPTION_PERSIST_COUNT;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
@StepScope
public class PageProcessingFailureListener implements ItemProcessListener<Object, Object> {

    private Integer maxExceptionCount = Integer.MAX_VALUE;

    private Integer currentSkipCount = 0;

    private final StepRunReportStore stepRunReportStore;

    private final StepRunReport stepRunReport = new StepRunReport();

    private final JobEventStore jobEventStore;

    private final PersistedErrorStore persistedErrorStore;


    public PageProcessingFailureListener(@Value("#{jobParameters['" + EXCEPTION_PERSIST_COUNT + "']}") Integer skipTolerance,
                                         @Value("#{jobParameters['" + JOB_EVENT_ID + "']}") String jobEventId,
                                         JobEventStore jobEventStore,
                                         StepRunReportStore stepRunReportStore, PersistedErrorStore persistedErrorStore) {
        this.stepRunReportStore = stepRunReportStore;
        this.jobEventStore = jobEventStore;
        this.persistedErrorStore = persistedErrorStore;

        stepRunReport.setJobEvent(jobEventStore.find(jobEventId));
        stepRunReportStore.create(stepRunReport);
    }


    @Override
    public void beforeProcess(Object item) {

    }

    @Override
    public void afterProcess(Object item, Object result) {

    }

    @Override
    public void onProcessError(Object item, Exception e) {
        if (currentSkipCount < maxExceptionCount) {
            PersistedError persistedError = stepRunReport.addError(e, item);
            persistedErrorStore.create(persistedError);
            jobEventStore.update(stepRunReport.getJobEvent());
        }
        currentSkipCount += 1;
    }
}
