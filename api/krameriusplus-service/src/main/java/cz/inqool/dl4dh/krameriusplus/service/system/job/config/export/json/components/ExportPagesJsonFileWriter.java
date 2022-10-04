package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@StepScope
public class ExportPagesJsonFileWriter extends JsonFileWriter<PageWithPathDto> {

    @Autowired
    public ExportPagesJsonFileWriter(JsonExporter exporter) {
        super(exporter);
    }

    @Override
    public void write(List<? extends PageWithPathDto> items) throws IOException {
        for (PageWithPathDto item : items) {
            exporter.export(item.getPage(), getItemOutputStream(item));
        }
    }
}
