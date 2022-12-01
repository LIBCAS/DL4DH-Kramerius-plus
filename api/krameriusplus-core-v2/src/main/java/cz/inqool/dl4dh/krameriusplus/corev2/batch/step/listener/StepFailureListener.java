package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.listener;

import cz.inqool.dl4dh.krameriusplus.api.exception.GeneralException;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.report.StepError;
import cz.inqool.dl4dh.krameriusplus.corev2.job.report.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.corev2.job.report.StepRunReportStore;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.KRAMERIUS_JOB_INSTANCE_ID;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.isNull;

/**
 * StepExecutionListener saves failure into JobExecution for later use
 *
 * @author Filip Kollar
 */
@StepScope
@Component
public class StepFailureListener extends StepExecutionListenerSupport {

    private static final String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_ERROR_OCCURRED";

    private static final String STOP_EXCEPTION_CODE = "STEP_STOPPED";

    @Value("#{jobParameters['" + KRAMERIUS_JOB_INSTANCE_ID + "']}")
    private String krameriusJobInstanceId;

    private KrameriusJobInstanceService jobInstanceService;

    private StepRunReportStore stepRunReportStore;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> failures = stepExecution.getFailureExceptions();

        if (!failures.isEmpty()) {
            String errorCode = extractErrorCode(failures.get(0));

            saveReport(stepExecution.getId(), failures);

            return new ExitStatus(errorCode);
        }

        return null;
    }

    private void saveReport(Long stepExecutionId, List<Throwable> failures) {
        KrameriusJobInstance krameriusJobInstance = jobInstanceService.find(krameriusJobInstanceId);

        StepRunReport existingReport = krameriusJobInstance.getReports().get(stepExecutionId);
        isNull(existingReport, () -> new IllegalStateException("Report with same stepExecutionId:" + stepExecutionId + " already exists."));

        StepRunReport report = new StepRunReport();
        report.setJob(krameriusJobInstance);
        report.setStepExecutionId(stepExecutionId);

        for (Throwable throwable : failures) {
            StepError error = new StepError();
            error.setStepRunReport(report);
            error.setExitCode(extractErrorCode(throwable));
            error.setShortMessage(throwable.getMessage());
            error.setStackTrace(ExceptionUtils.getStackTrace(throwable));

            report.getErrors().add(error);
        }

        stepRunReportStore.create(report);
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
    public void setJobInstanceService(KrameriusJobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }

    @Autowired
    public void setStepRunReportStore(StepRunReportStore stepRunReportStore) {
        this.stepRunReportStore = stepRunReportStore;
    }
}
