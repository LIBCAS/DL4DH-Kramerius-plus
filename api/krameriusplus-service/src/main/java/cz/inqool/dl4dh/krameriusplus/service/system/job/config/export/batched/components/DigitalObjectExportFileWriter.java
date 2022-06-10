package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.system.export.exporter2.ExporterMediator2;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.DIRECTORY;


public abstract class DigitalObjectExportFileWriter<T extends DigitalObject> implements ItemWriter<T> {

    private Path exportDirectory;

    private final ExporterMediator2 exporterMediator;

    private final ExportFormat exportFormat;

    protected DigitalObjectExportFileWriter(ExporterMediator2 exporterMediator, ExportFormat exportFormat) {
        this.exporterMediator = exporterMediator;
        this.exportFormat = exportFormat;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            writeItem(item);
        }
    }

    private void writeItem(T item) throws IOException {
        OutputStream outputStream = getItemOutputStream(item);

        exporterMediator.export(outputStream, item, exportFormat);
    }

    private OutputStream getItemOutputStream(T item) throws IOException {
        return Files.newOutputStream(exportDirectory.resolve(Path.of(getItemFileName(item))));
    }

    private String getItemFileName(T item) {
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
