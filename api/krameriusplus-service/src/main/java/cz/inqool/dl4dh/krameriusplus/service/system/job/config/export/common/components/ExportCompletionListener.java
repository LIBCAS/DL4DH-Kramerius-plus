package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExport;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequestStore;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
public class ExportCompletionListener implements JobExecutionListener {

    private final ExportStore exportStore;

    private final ExportRequestStore exportRequestStore;

    private final BulkExportStore bulkExportStore;

    @Autowired
    public ExportCompletionListener(ExportStore exportStore,
                                    ExportRequestStore exportRequestStore,
                                    BulkExportStore bulkExportStore) {
        this.exportStore = exportStore;
        this.exportRequestStore = exportRequestStore;
        this.bulkExportStore = bulkExportStore;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // do nothing
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        String jobEventId = jobExecution.getJobParameters().getString(JOB_EVENT_ID);
        ExportRequest request = exportRequestStore.findByJobEventId(jobEventId);
        BulkExport bulkExport = request.getBulkExport();

        if (jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            // find entities for request and update
            Export export = exportStore.findByJobEvent(jobEventId);

            bulkExport.getExports().add(export);

            bulkExportStore.update(bulkExport);
        }
    }
}
