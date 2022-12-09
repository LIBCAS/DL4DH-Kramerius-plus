package cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.JobPlanStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.ScheduledJobEvent;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.ZipArchiver;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.FILE_REF_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;


/**
 * Tasklet unzips all files created from the same jobPlan
 */
@Component
@StepScope
public class UnzipExportsTasklet extends ValidatedTasklet {

    public static final String TMP_PATH = "data/tmp/";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    private final JobPlanStore jobPlanStore;

    private final ExportStore exportStore;

    private final FileService fileService;

    private final ZipArchiver zipArchiver;

    @Autowired
    public UnzipExportsTasklet(JobPlanStore jobPlanStore,
                               ExportStore exportStore,
                               FileService fileService, ZipArchiver zipArchiver) {
        this.jobPlanStore = jobPlanStore;
        this.exportStore = exportStore;
        this.fileService = fileService;
        this.zipArchiver = zipArchiver;
    }

    @Override
    protected RepeatStatus executeValidatedTasklet(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobEventId = (String) chunkContext.getStepContext().getJobParameters().get(JOB_EVENT_ID);

        List<Export> exports = getExports(jobEventId);

        // assign fileref and set status to finish the whole job
        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

        if (exports.size() == 1) {
            executionContext.putString(FILE_REF_ID, exports.get(0).getFileRef().getId());
            contribution.getStepExecution().setExitStatus(new ExitStatus("MERGE_DONE"));

            return RepeatStatus.FINISHED;
        }

        // make path and prepare context for ZipExportTasklet
        String exportType = getExportType(exports.get(0).getFileRef().getName());
        Path unzippedPath = Files.createDirectory(Path.of(TMP_PATH + buildDirectoryName(jobEventId, exportType + "_SET")));

        unZipIntoDir(unzippedPath, exports);

        executionContext.putString(DIRECTORY, unzippedPath.toString()); // necessary for zip tasklet

        return RepeatStatus.FINISHED;
    }

    private void unZipIntoDir(Path dir, List<Export> exports) throws IOException{
        for (Export export : exports) {
            FileRef fileRef = export.getFileRef();
            Path exportZipPath = fileService.find(fileRef.getId()).getPath();

            zipArchiver.unzip(exportZipPath, exportZipPath.getParent().resolve(exportZipPath.getFileName() + ".zip"));
        }
    }

    private List<Export> getExports(String jobEventId) {
        return jobPlanStore.findByJobEvent(jobEventId)
                .getScheduledJobEvents()
                .stream()
                .map(ScheduledJobEvent::getJobEvent)
                .map(jobEvent -> exportStore.findByJobEvent(jobEvent.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String buildDirectoryName(String objectId, String prefix) {
        return prefix + "_" + objectId + "_" + formatter.format(LocalDateTime.now());
    }

    private String getExportType(String exportName) {
        return exportName.substring(0, exportName.indexOf("_", 7));
    }
}
