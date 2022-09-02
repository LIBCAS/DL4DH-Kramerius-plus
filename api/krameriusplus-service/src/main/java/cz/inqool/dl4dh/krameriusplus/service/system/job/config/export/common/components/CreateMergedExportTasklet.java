package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.export.MergedExport;
import cz.inqool.dl4dh.krameriusplus.core.system.export.MergedExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanStore;
import org.springframework.batch.core.JobParameters;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.JOB_PLAN_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.ZIPPED_FILE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
@StepScope
public class CreateMergedExportTasklet implements Tasklet {

    private final FileService fileService;

    private final ExportStore exportStore;

    private final MergedExportStore mergedExportStore;

    private final JobEventStore jobEventStore;

    private final JobPlanStore jobPlanStore;

    @Autowired
    public CreateMergedExportTasklet(FileService fileService,
                                     ExportStore exportStore,
                                     MergedExportStore mergedExportStore,
                                     JobEventStore jobEventStore,
                                     JobPlanStore jobPlanStore) {
        this.fileService = fileService;
        this.exportStore = exportStore;
        this.mergedExportStore = mergedExportStore;
        this.jobPlanStore = jobPlanStore;
        this.jobEventStore = jobEventStore;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String path = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(ZIPPED_FILE);
        Path zippedFile = Path.of(path);

        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();

        FileRef fileRef;

        try (InputStream is = new FileInputStream(zippedFile.toFile())) {
            fileRef = fileService.create(is,
                    Files.size(zippedFile),
                    zippedFile.getFileName().toString(),
                    "application/zip");
        }

        String jobPlanId = stepExecution.getJobExecution().getExecutionContext().getString(JOB_PLAN_ID);
        JobEvent jobEvent = jobEventStore.find(jobParameters.getString(JOB_EVENT_ID));

        MergedExport mergedExport = createMergeExport(fileRef, jobEvent, jobPlanId);

        mergedExportStore.create(mergedExport);

        return RepeatStatus.FINISHED;
    }

    private MergedExport createMergeExport(FileRef fileRef, JobEvent jobEvent, String jobPlanId) {
        List<Export> exports = jobPlanStore.findAllInPlan(jobPlanId)
                .stream()
                .map(event -> exportStore.findByJobEvent(event.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        MergedExport mergedExport = new MergedExport();
        mergedExport.setJobEvent(jobEvent);
        mergedExport.setFileRef(fileRef);
        mergedExport.getExports().addAll(exports);

        return mergedExport;
    }
}
