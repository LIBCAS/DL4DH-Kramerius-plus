package cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.CsvExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.DigitalObjectWithPathDto;
import org.springframework.batch.core.StepExecution;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.DELIMITER;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

public abstract class CsvFileWriter<T> extends FileWriter<T> {

    protected CsvExporter exporter;

    @Override
    protected String getItemFileName(DigitalObjectWithPathDto item) {
        if (item.getDigitalObject() instanceof Publication) {
            return "metadata.csv";
        } else if (item.getDigitalObject() instanceof Page) {
            return getPageFilename(((Page) item.getDigitalObject()), "csv");
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
