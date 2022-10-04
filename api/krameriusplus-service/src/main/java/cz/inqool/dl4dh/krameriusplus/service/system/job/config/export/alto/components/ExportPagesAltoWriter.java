package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoStringDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@StepScope
public class ExportPagesAltoWriter extends FileWriter<PageAndAltoStringDto> {
    @Override
    public void write(List<? extends PageAndAltoStringDto> items) throws Exception {
        for (PageAndAltoStringDto item : items) {
            String alto = item.getAltoString();

            try (OutputStream out = getItemOutputStream(item)) {
                out.write(alto.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    @Override
    protected String getItemFileName(DigitalObjectWithPathDto item) {
        if (!(item instanceof PageAndAltoStringDto || !(item.getDigitalObject() instanceof Page))) {
            throw new IllegalStateException(item.getClass().getSimpleName() + " not allowed in ALTO export format");
        }
        return getPageFilename((Page) item.getDigitalObject(), "xml");
    }
}
