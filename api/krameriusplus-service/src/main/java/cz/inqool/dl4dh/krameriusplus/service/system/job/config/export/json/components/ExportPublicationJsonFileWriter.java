package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.JsonExporter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;

@Component
@StepScope
public class ExportPublicationJsonFileWriter extends JsonFileWriter<DigitalObjectWithPathDto> {

    @Autowired
    public ExportPublicationJsonFileWriter(JsonExporter exporter) {
        super(exporter);
    }

    @Override
    public void write(List<? extends DigitalObjectWithPathDto> items) throws IOException {
        eq(items.size(), 1, () -> new IllegalStateException("Expected one publication on write, got: " + items.size()));

        DigitalObjectWithPathDto digitalObjectWithPathDto = items.get(0);
        exporter.export(((Publication) digitalObjectWithPathDto.getDigitalObject()), getItemOutputStream(digitalObjectWithPathDto));
    }
}
