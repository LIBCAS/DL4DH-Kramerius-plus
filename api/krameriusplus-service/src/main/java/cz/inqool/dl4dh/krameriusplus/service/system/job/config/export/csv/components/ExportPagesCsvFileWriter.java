package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class ExportPagesCsvFileWriter extends CsvFileWriter<Page> {

    @Override
    public void write(List<? extends Page> items) throws Exception {
        for (Page item : items) {
            exporter.export(item, getItemOutputStream(item));
        }
    }
}
