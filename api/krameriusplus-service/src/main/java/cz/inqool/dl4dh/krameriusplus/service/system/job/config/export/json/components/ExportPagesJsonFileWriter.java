package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@StepScope
public class ExportPagesJsonFileWriter extends JsonFileWriter<Page> {

    @Autowired
    public ExportPagesJsonFileWriter(JsonExporter exporter) {
        super(exporter);
    }

    @Override
    public void write(List<? extends Page> items) throws IOException {
        for (Page item : items) {
            exporter.export(item, getItemOutputStream(item));
        }
    }
}
