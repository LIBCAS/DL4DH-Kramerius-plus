package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.DIRECTORY;

public abstract class JsonFileWriter<T extends DigitalObject> implements ItemWriter<T> {

    protected Path exportDirectory;

    protected final JsonExporter exporter;

    protected final ExportFormat exportFormat = ExportFormat.JSON;

    protected JsonFileWriter(JsonExporter exporter) {
        this.exporter = exporter;
    }

    protected OutputStream getItemOutputStream(DigitalObject item) throws IOException {
        return Files.newOutputStream(exportDirectory.resolve(Path.of(getItemFileName(item))));
    }

    private String getItemFileName(DigitalObject item) {
        String itemClassName = item.getClass().getSimpleName();
        String suffix = Character.toLowerCase(itemClassName.charAt(0)) + itemClassName.substring(1);

        return exportFormat.getFileName(suffix, item.getId());
    }

    @BeforeStep
    public void injectExecutionContext(StepExecution stepExecution) {
        String path = stepExecution.getJobExecution().getExecutionContext().getString(DIRECTORY);
        exportDirectory = Path.of(path);
    }
}
