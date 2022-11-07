package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.components;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@StepScope
public class ExportPagesTextWriter extends FileWriter<PageAndAltoDto> {

    private final AltoMetadataExtractor altoMetadataExtractor;

    @Autowired
    public ExportPagesTextWriter(AltoMetadataExtractor altoMetadataExtractor) {
        this.altoMetadataExtractor = altoMetadataExtractor;
    }

    @Override
    protected String getItemFileName(DigitalObjectWithPathDto item) {
        if (!(item instanceof PageAndAltoDto)) {
            throw new IllegalStateException(item.getClass().getSimpleName() + " not allowed in TEXT export");
        }
        return getPageFilename(((Page) item.getDigitalObject()), "txt");
    }


    @Override
    public void write(List<? extends PageAndAltoDto> items) throws Exception {
        for (PageAndAltoDto item : items) {
            Alto alto = item.getAlto();

            try (OutputStream out = getItemOutputStream(item)) {
                String pageContent = altoMetadataExtractor.extractText(alto);
                out.write(pageContent.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
