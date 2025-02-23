package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.RequestState;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportState;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import cz.inqool.dl4dh.krameriusplus.core.utils.ZipArchiver;
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

        String tmpName = buildTmpName(exportRequest);
        Path tmpUnzipDirectory = Files.createDirectory(tmpDirPath.resolve(tmpName));
        Path resultPath = tmpDirPath.resolve(tmpName + ".zip");

        if (allIncomplete(exportRequest)) {
            exportRequest.getBulkExport().setState(ExportState.FAILED);
            exportRequest.setState(RequestState.FAILED);
            return RepeatStatus.FINISHED;
        }

        for (Export export : exportRequest.getItems().stream()
                .map(ExportRequestItem::getRootExport)
                .collect(Collectors.toList())) {
            FileRef fileRef = export.getFileRef();
            if (fileRef != null) {
                try (InputStream inputStream = fileService.find(fileRef.getId()).open()){
                    zipArchiver.unzip(inputStream, tmpUnzipDirectory.resolve(export.getPublicationId().substring(5)));
                }
            }
        }

        zipArchiver.zip(tmpUnzipDirectory, resultPath);

        boolean containsIncompleteExports = containsIncomplete(exportRequest);

        try (InputStream inputStream = Files.newInputStream(resultPath)) {
            FileRef fileRef = fileService.create(
                    inputStream,
                    Files.size(resultPath),
                    tmpName + ".zip",
                    "application/zip");
            exportRequest.getBulkExport().setFile(fileRef);
            exportRequest.getBulkExport().setState(containsIncompleteExports ? ExportState.PARTIAL : ExportState.SUCCESSFUL);
            exportRequest.setState(containsIncompleteExports ? RequestState.PARTIAL : RequestState.COMPLETED);
            exportRequestStore.save(exportRequest);
        }

        FileUtils.deleteDirectory(tmpUnzipDirectory.toFile());
        Files.delete(resultPath);

        return RepeatStatus.FINISHED;
    }

    private boolean containsIncomplete(ExportRequest exportRequest) {
        return exportRequest.getItems().stream()
                .anyMatch(item -> !item.getRootExport().getState().equals(ExportState.SUCCESSFUL));
    }

    private boolean allIncomplete(ExportRequest exportRequest) {
        return exportRequest.getItems().stream()
                .map(ExportRequestItem::getRootExport)
                .allMatch(export -> export.getFileRef() == null);
    }

    private String buildTmpName(ExportRequest exportRequest) {
        return "BULK_" + exportRequest.getConfig().getExportFormat() + "_EXPORT_REQUEST_UUID_" + exportRequest.getId();
    }

    @Autowired
    public void setTmpDirPath(@Value("${system.export.tmp-dir}") String tmpDirPath) {
        this.tmpDirPath = Path.of(tmpDirPath);
    }

    @Autowired
    public void setExportRequestId(@Value("#{jobParameters['" + JobParameterKey.EXPORT_REQUEST_ID + "']}") String exportRequestId) {
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
