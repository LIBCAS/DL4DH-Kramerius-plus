package cz.inqool.dl4dh.krameriusplus.corev2.job.report;

import cz.inqool.dl4dh.krameriusplus.api.exception.GeneralException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceStore;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StepRunReportService {

    private static final String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_ERROR_OCCURRED";

    private static final String STOP_EXCEPTION_CODE = "STEP_STOPPED";

    private StepRunReportStore store;

    private KrameriusJobInstanceStore jobInstanceStore;

    @Transactional
    public StepRunReport saveStepErrors(String jobInstanceId, Long stepExecutionId, List<Throwable> errors) {
        KrameriusJobInstance krameriusJobInstance = jobInstanceStore.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));
        StepRunReport report = krameriusJobInstance.getReports().get(stepExecutionId);

        if (report == null) {
            report = new StepRunReport();
            report.setJob(krameriusJobInstance);
            report.setStepExecutionId(stepExecutionId);
        }

        for (Throwable error : errors) {
            StepError stepError = new StepError();
            stepError.setStepRunReport(report);
            stepError.setExitCode(extractErrorCode(error));
            stepError.setShortMessage(error.getMessage());
            stepError.setStackTrace(ExceptionUtils.getStackTrace(error));

            report.getErrors().add(stepError);
        }

        return store.save(report);
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
