package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class ExportPagesCsvFileWriter extends CsvFileWriter<PageWithPathDto> {

    @Override
    public void write(List<? extends PageWithPathDto> items) throws Exception {
        for (PageWithPathDto item : items) {
            exporter.export(item.getPage(), this.getItemOutputStream(item));
        }
    }
}
