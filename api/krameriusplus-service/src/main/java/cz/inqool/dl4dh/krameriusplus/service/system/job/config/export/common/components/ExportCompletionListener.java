package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExport;
import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.ScheduledJobEvent;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
public class ExportCompletionListener implements JobExecutionListener {

    private final ExportStore exportStore;

    private final BulkExportStore bulkExportStore;

    private final JobPlanStore jobPlanStore;

    @Autowired
    public ExportCompletionListener(ExportStore exportStore, BulkExportStore bulkExportStore, JobPlanStore jobPlanStore) {
        this.exportStore = exportStore;
        this.bulkExportStore = bulkExportStore;
        this.jobPlanStore = jobPlanStore;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // do nothing
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        String jobEventId = jobExecution.getJobParameters().getString(JOB_EVENT_ID);
        Optional<JobEvent> mergeJob = jobPlanStore.findByJobEvent(jobEventId)
                .getScheduledJobEvents()
                .stream().map(ScheduledJobEvent::getJobEvent)
                .filter(jobEvent -> jobEvent.getConfig().getKrameriusJob() == KrameriusJob.EXPORT_MERGE)
                .findFirst();

        if (mergeJob.isPresent() && jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            BulkExport bulkExport = bulkExportStore.findByJobEventId(mergeJob.get().getId());
            Export export = exportStore.findByJobEvent(jobEventId);

            bulkExport.getExports().add(export);

            bulkExportStore.update(bulkExport);
        }
    }
}
