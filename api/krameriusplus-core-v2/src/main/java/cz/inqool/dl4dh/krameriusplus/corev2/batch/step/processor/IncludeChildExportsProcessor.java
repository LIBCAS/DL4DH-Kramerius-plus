package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.file.FileService;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.utils.ZipArchiver;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;

@Component
@StepScope
public class IncludeChildExportsProcessor implements ItemProcessor<Export, Export> {

    private FileService fileService;

    private ZipArchiver zipArchiver;

    private Path directory;

    @Override
    public Export process(Export item) throws Exception {
        Path childPath = directory.resolve(item.getPublicationId().substring(5));

        try (InputStream is = fileService.find(item.getFileRef().getId()).open()) {
            zipArchiver.unzip(is, childPath);
        }

        return item;
    }

    @Autowired
    public void setDirectory(@Value("#{jobExecutionContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setZipArchiver(ZipArchiver zipArchiver) {
        this.zipArchiver = zipArchiver;
    }
}
