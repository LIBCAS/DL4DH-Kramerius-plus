package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.export.BulkExportState;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileService;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import cz.inqool.dl4dh.krameriusplus.corev2.utils.ZipArchiver;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Component
@StepScope
public class MergeExportsTasklet implements Tasklet {

    private Path tmpDirPath;

    private String exportRequestId;

    private ZipArchiver zipArchiver;

    private ExportRequestStore exportRequestStore;

    private FileService fileService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExportRequest exportRequest = exportRequestStore.findById(exportRequestId).orElseThrow();

        Path tmpUnzipDirectory = tmpDirPath.resolve(buildTmpName(exportRequest));
        Files.createDirectory(tmpUnzipDirectory);

        boolean isComplete = true;
        for (Export export : exportRequest.getItems().stream()
                .map(ExportRequestItem::getRootExport)
                .collect(Collectors.toList())) {
            FileRef fileRef = export.getFileRef();
            if (fileRef != null) {
                zipArchiver.unzip(fileService.find(fileRef.getId()).open(), tmpUnzipDirectory);
            }
            else {
                isComplete = false;
            }
        }


        String zipName = buildTmpName(exportRequest) + ".zip";
        Path resultPath = tmpDirPath.resolve(zipName);
        zipArchiver.zip(tmpUnzipDirectory, resultPath);

        try (InputStream inputStream = Files.newInputStream(resultPath)) {
            FileRef fileRef = fileService.create(
                    inputStream,
                    Files.size(resultPath),
                    zipName,
                    "application/zip");
            exportRequest.getBulkExport().setFile(fileRef);
            exportRequest.getBulkExport().setState(isComplete ? BulkExportState.PARTIAL : BulkExportState.SUCCESSFUL);
            exportRequestStore.save(exportRequest);
        }

        FileUtils.deleteDirectory(tmpUnzipDirectory.toFile());
        Files.delete(resultPath);

        return RepeatStatus.FINISHED;
    }

    private String buildTmpName(ExportRequest exportRequest) {
        return "BULK_" + exportRequest.getConfig().getExportFormat() + "_EXPORT_REQUEST_UUID_" + exportRequest.getId();
    }

    @Autowired
    public void setTmpDirPath(@Value("${export.tmp-dir}") String tmpDirPath) {
        this.tmpDirPath = Path.of(tmpDirPath);
    }

    @Autowired
    public void setExportRequestId(@Value("#{jobParameters['" + EXPORT_REQUEST_ID + "']}") String exportRequestId) {
        this.exportRequestId = exportRequestId;
    }

    @Autowired
    public void setZipArchiver(ZipArchiver zipArchiver) {
        this.zipArchiver = zipArchiver;
    }

    @Autowired
    public void setExportRequestStore(ExportRequestStore exportRequestStore) {
        this.exportRequestStore = exportRequestStore;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
