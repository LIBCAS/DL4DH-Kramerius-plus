package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.CsvExporter;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.DELIMITER;

@Component
@StepScope
public class CsvExportPageProcessor implements ItemProcessor<Page, DigitalObjectExport> {

    private CsvExporter csvExporter;

    @Override
    public DigitalObjectExport process(Page item) throws Exception {
        item.setTeiBodyFileId(null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        csvExporter.export(item, byteArrayOutputStream);

        return new DigitalObjectExport(item, byteArrayOutputStream.toString(StandardCharsets.UTF_8),
                "page_" + item.getId().substring(5) + ".csv");
    }

    @Autowired
    public void setCsvExporter(@Value("#{jobParameters['" + DELIMITER + "']}") String delimiter) {
        if (delimiter.length() != 1) {
            throw new IllegalArgumentException("Delimiter: " + delimiter + "is not of required length 1");
        }

        this.csvExporter = new CsvExporter(delimiter.charAt(0));
    }
}
