package cz.inqool.dl4dh.krameriusplus.core.batch.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.report.StepError;
import cz.inqool.dl4dh.krameriusplus.core.job.report.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.job.report.StepRunReportService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * StepExecutionListener to save step failures into KrameriusJobInstance and set the ExitCode in case of errors.
 *
 * @author Filip Kollar
 */
@StepScope
@Component
public class StepFailureListener extends StepExecutionListenerSupport {

    @Value("#{jobParameters['" + JobParameterKey.KRAMERIUS_JOB_INSTANCE_ID + "']}")
    private String krameriusJobInstanceId;

    private StepRunReportService reportService;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> failures = stepExecution.getFailureExceptions();

        if (!failures.isEmpty()) {
            StepRunReport report = reportService.saveStepErrors(krameriusJobInstanceId, stepExecution.getId(), failures);

            Optional<StepError> firstError = report.getErrors().stream().min(Comparator.comparing(StepError::getOrder));
            if (firstError.isEmpty()) {
                throw new IllegalStateException("First error was not found even though step errors were persisted.");
            }

            return new ExitStatus(firstError.get().getExitCode());
        }

        return null;
    }

    @Autowired
    public void setReportService(StepRunReportService reportService) {
        this.reportService = reportService;
    }
}
