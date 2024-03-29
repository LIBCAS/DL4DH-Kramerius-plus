package cz.inqool.dl4dh.krameriusplus.core.batch.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.report.StepRunReportService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class StepSkipListener extends SkipListenerSupport<Page, Page> {

    @Value("#{stepExecution.id}")
    private Long stepExecutionId;

    @Value("#{jobParameters['" + JobParameterKey.KRAMERIUS_JOB_INSTANCE_ID + "']}")
    private String krameriusJobInstanceId;

    private StepRunReportService reportService;

    @Override
    public void onSkipInWrite(Page item, Throwable t) {
        reportService.saveStepErrors(krameriusJobInstanceId, stepExecutionId, List.of(t));
    }

    @Override
    public void onSkipInProcess(Page item, Throwable t) {
        reportService.saveStepErrors(krameriusJobInstanceId, stepExecutionId, List.of(t));
    }

    @Autowired
    public void setReportService(StepRunReportService reportService) {
        this.reportService = reportService;
    }
}
