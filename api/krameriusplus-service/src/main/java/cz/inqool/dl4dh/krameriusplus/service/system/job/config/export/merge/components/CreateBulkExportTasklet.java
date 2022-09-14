package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.merge.components;

import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExport;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequestStore;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.FILE_REF_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@StepScope
public class CreateBulkExportTasklet implements Tasklet {

    private final FileService fileService;

    private final BulkExportStore bulkExportStore;

    private final ExportRequestStore exportRequestStore;

    @Autowired
    public CreateBulkExportTasklet(FileService fileService,
                                   BulkExportStore bulkExportStore,
                                   ExportRequestStore exportRequestStore) {
        this.fileService = fileService;
        this.bulkExportStore = bulkExportStore;
        this.exportRequestStore = exportRequestStore;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String fileRefId = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(FILE_REF_ID);
        String jobEventId = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getString(JOB_EVENT_ID);
        notNull(fileRefId, () -> new IllegalStateException("Missing FILE_REF_ID in execution context."));

        FileRef fileRef = fileService.find(fileRefId);

        ExportRequest exportRequest = exportRequestStore.findByJobEventId(jobEventId);

        BulkExport bulkExport = exportRequest.getBulkExport();
        bulkExport.setFileRef(fileRef);
        bulkExportStore.update(bulkExport);

        if (bulkExport.getExports().size() == 1) {
            contribution.getStepExecution().setExitStatus(new ExitStatus("NO_CLEANUP"));
        }

        return RepeatStatus.FINISHED;
    }
}
