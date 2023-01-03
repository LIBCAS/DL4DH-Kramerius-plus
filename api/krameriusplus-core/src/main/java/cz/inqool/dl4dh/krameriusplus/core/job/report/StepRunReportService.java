package cz.inqool.dl4dh.krameriusplus.core.job.report;

import cz.inqool.dl4dh.krameriusplus.api.exception.GeneralException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceStore;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class StepRunReportService {

    private static final String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_ERROR_OCCURRED";

    private static final String STOP_EXCEPTION_CODE = "STEP_STOPPED";

    private StepRunReportStore store;

    private KrameriusJobInstanceStore jobInstanceStore;

    @Transactional
    public StepRunReport saveStepErrors(String jobInstanceId, Long stepExecutionId, List<Throwable> errors) {
        notNull(jobInstanceId, () -> new IllegalArgumentException("jobInstanceId"));
        notNull(stepExecutionId, () -> new IllegalArgumentException("stepExecutionId"));

        KrameriusJobInstance krameriusJobInstance = jobInstanceStore.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));
        StepRunReport report = krameriusJobInstance.getReports().get(stepExecutionId);

        if (report == null) {
            report = new StepRunReport();
            report.setJob(krameriusJobInstance);
            report.setStepExecutionId(stepExecutionId);
        }

        for (Throwable error : errors) {
            Throwable unwrapped = unwrapThrowable(error);
            StepError stepError = new StepError();
            stepError.setStepRunReport(report);
            stepError.setExitCode(extractErrorCode(unwrapped));
            stepError.setShortMessage(unwrapped.getMessage());
            stepError.setStackTrace(ExceptionUtils.getStackTrace(unwrapped));

            report.getErrors().add(stepError);
        }

        return store.save(report);
    }

    private Throwable unwrapThrowable(Throwable error) {
        if (error instanceof SkipLimitExceededException) {
            return unwrapThrowable(error.getCause());
        }

        return error;
    }

    private String extractErrorCode(Throwable exception) {
        if (exception instanceof GeneralException) {
            return ((GeneralException) exception).getErrorCode().name();
        }
        else if (exception instanceof JobInterruptedException) {
            return STOP_EXCEPTION_CODE;
        }
        else {
            return UNEXPECTED_EXCEPTION_CODE;
        }
    }

    @Autowired
    public void setStore(StepRunReportStore store) {
        this.store = store;
    }

    @Autowired
    public void setJobInstanceStore(KrameriusJobInstanceStore jobInstanceStore) {
        this.jobInstanceStore = jobInstanceStore;
    }
}
