package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExport;
import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.ZIPPED_FILE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
@StepScope
public class CreateBulkExportTasklet implements Tasklet {

    private final FileService fileService;

    private final BulkExportStore bulkExportStore;

    private final JobEventStore jobEventStore;

    @Autowired
    public CreateBulkExportTasklet(FileService fileService,
                                   BulkExportStore bulkExportStore,
                                   JobEventStore jobEventStore) {
        this.fileService = fileService;
        this.bulkExportStore = bulkExportStore;
        this.jobEventStore = jobEventStore;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String path = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(ZIPPED_FILE);
        Path zippedFile = Path.of(path);

        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();

        FileRef fileRef;

        try (InputStream is = new FileInputStream(zippedFile.toFile())) {
            fileRef = fileService.create(is,
                    Files.size(zippedFile),
                    zippedFile.getFileName().toString(),
                    "application/zip");
        }

        JobEvent jobEvent = jobEventStore.find(stepExecution.getJobExecution().getJobParameters().getString(JOB_EVENT_ID));

        BulkExport bulkExport = new BulkExport();
        bulkExport.setJobEvent(jobEvent);
        bulkExport.setFileRef(fileRef);
        bulkExportStore.create(bulkExport);

        return RepeatStatus.FINISHED;
    }
}
