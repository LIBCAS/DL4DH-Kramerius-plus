package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.CsvExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;
import org.springframework.batch.core.StepExecution;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.DELIMITER;

public abstract class CsvFileWriter<T extends DigitalObject> extends FileWriter<T> {

    protected CsvExporter exporter;

    protected OutputStream getItemOutputStream(DigitalObject item) throws IOException {
        return Files.newOutputStream(exportDirectory.resolve(Path.of(getItemFileName(item))));
    }

    @Override
    protected String getItemFileName(DigitalObject item) {
        if (item instanceof Publication) {
            return "metadata.csv";
        } else if (item instanceof Page) {
            return String.format("page%04d_uuid_%s.csv", item.getIndex(), item.getId().substring(5));
        }

        throw new IllegalStateException("Invalid type of item in CSV export: '" + item.getClass().getSimpleName() + "'.");
    }

    @Override
    public void doBeforeStep(StepExecution stepExecution) {
        String delimiter = stepExecution.getJobExecution().getJobParameters().getString(DELIMITER);
        notNull(delimiter, () -> new IllegalArgumentException("Delimiter cannot be null."));
        eq(delimiter.length(), 1, () -> new IllegalArgumentException("Delimiter cannot be a multichar string."));

        exporter = new CsvExporter(delimiter.charAt(0));
    }

}
