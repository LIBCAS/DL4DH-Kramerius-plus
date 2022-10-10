package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;

@Component
@StepScope
public class ExportPublicationCsvFileWriter extends CsvFileWriter<DigitalObjectWithPathDto> {

    @Override
    public void write(List<? extends DigitalObjectWithPathDto> items) throws Exception {
        eq(items.size(), 1, () -> new IllegalStateException("Expected one publication on write, got: " + items.size()));

        DigitalObjectWithPathDto digitalObjectWithPathDto = items.get(0);
        exporter.export(((Publication) digitalObjectWithPathDto.getDigitalObject()), getItemOutputStream(digitalObjectWithPathDto));
    }
}
