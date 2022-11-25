package cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.DigitalObjectWithPathDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class ExportPagesCsvFileWriter extends CsvFileWriter<DigitalObjectWithPathDto> {

    @Override
    public void write(List<? extends DigitalObjectWithPathDto> items) throws Exception {
        for (DigitalObjectWithPathDto item : items) {
            exporter.export(((Page) item.getDigitalObject()), this.getItemOutputStream(item));
        }
    }
}
