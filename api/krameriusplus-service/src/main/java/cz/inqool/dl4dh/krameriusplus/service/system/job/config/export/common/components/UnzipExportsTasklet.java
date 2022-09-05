package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.JOB_PLAN_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;


/**
 * Tasklet unzips all files created from the same jobPlan
 */
@Component
@StepScope
public class UnzipExportsTasklet implements Tasklet {

    public static final String TMP_PATH = "data/tmp/";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    private final JobPlanStore jobPlanStore;

    private final ExportStore exportStore;

    private final FileService fileService;

    @Autowired
    public UnzipExportsTasklet(JobPlanStore jobPlanStore,
                               ExportStore exportStore,
                               FileService fileService) {
        this.jobPlanStore = jobPlanStore;
        this.exportStore = exportStore;
        this.fileService = fileService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobEventId = (String) chunkContext.getStepContext().getJobParameters().get(JOB_EVENT_ID);

        String jobPlanId = jobPlanStore.findByJobEvent(jobEventId).getId();


        List<Export> exports = jobPlanStore.findAllInPlan(jobPlanId)
                .stream()
                .map(jobEvent -> exportStore.findByJobEvent(jobEvent.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        String exportType = getExportType(exports.get(0).getFileRef().getName());
        Path unzippedPath = Files.createDirectory(Path.of(TMP_PATH + buildDirectoryName(jobPlanId, exportType + "_SET")));

        for (Export export : exports) {
            FileRef fileRef = export.getFileRef();
            try (InputStream is = fileService.find(fileRef.getId()).open()) {
                Path exportPath = Files.createDirectory(Path.of(unzippedPath + File.separator + buildDirectoryName(export.getPublicationId(), exportType)));

                unZip(is, exportPath.toFile());
            }
        }
        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        executionContext.put(DIRECTORY, unzippedPath.toString()); // necessary for zip tasklet
        executionContext.put(JOB_PLAN_ID, jobPlanId);

        return RepeatStatus.FINISHED;
    }


    /**
     * code from <a href="https://www.baeldung.com/java-compress-and-uncompress">baeldung</a>
     *
     * @param zipFileIs input stream of file to unzip
     * @param outDir directory for output
     * @throws IOException in case of FS issues
     */
    private void unZip(InputStream zipFileIs, File outDir) throws IOException{
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(zipFileIs);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(outDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    /**
     * code from <a href="https://www.baeldung.com/java-compress-and-uncompress">baeldung</a>
     * @param destinationDir directory
     * @param zipEntry one zipEntry
     * @return File to created dir
     * @throws IOException in case of FS issues
     */
    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private String buildDirectoryName(String objectId, String prefix) {
        if (objectId.contains("uuid")) {
            objectId = objectId.substring(5);
        }

        return prefix + "_" + objectId + "_" + formatter.format(LocalDateTime.now());
    }

    private String getExportType(String exportName) {
        return exportName.substring(0, exportName.indexOf("_", 7));
    }
}
