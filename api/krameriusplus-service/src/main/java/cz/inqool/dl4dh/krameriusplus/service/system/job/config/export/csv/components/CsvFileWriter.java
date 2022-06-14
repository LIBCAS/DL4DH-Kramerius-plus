package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.CsvExporter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.DELIMITER;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.DIRECTORY;

public abstract class CsvFileWriter<T extends DigitalObject> implements ItemWriter<T> {

    protected Path exportDirectory;

    protected CsvExporter exporter;

    protected OutputStream getItemOutputStream(DigitalObject item) throws IOException {
        return Files.newOutputStream(exportDirectory.resolve(Path.of(getItemFileName(item))));
    }

    private String getItemFileName(DigitalObject item) {
        if (item instanceof Publication) {
            return "metadata.csv";
        } else if (item instanceof Page) {
            return String.format("page%04d_%s.csv", item.getIndex(), item.getId());
        }

        throw new IllegalStateException("Invalid type of item in CSV export: '" + item.getClass().getSimpleName() + "'.");
    }

    @BeforeStep
    public void injectExecutionContext(StepExecution stepExecution) {
        String path = stepExecution.getJobExecution().getExecutionContext().getString(DIRECTORY);
        exportDirectory = Path.of(path);

        String delimiter = stepExecution.getJobExecution().getJobParameters().getString(DELIMITER);

        exporter = new CsvExporter(delimiter);
    }

}
