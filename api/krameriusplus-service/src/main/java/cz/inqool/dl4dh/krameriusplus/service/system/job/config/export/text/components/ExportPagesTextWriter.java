package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoContentExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@StepScope
public class ExportPagesTextWriter extends FileWriter<Page> {

    private final StreamProvider streamProvider;

    private final AltoMapper altoMapper;

    @Autowired
    public ExportPagesTextWriter(StreamProvider streamProvider, AltoMapper altoMapper) {
        this.streamProvider = streamProvider;
        this.altoMapper = altoMapper;
    }

    @Override
    public void write(List<? extends Page> items) throws Exception {
        for (Page item : items) {
            AltoDto alto = altoMapper.toAltoDto(streamProvider.getAlto(item.getId()));

            try (OutputStream out = getItemOutputStream(item)) {
                String pageContent = new AltoContentExtractor().extractText(alto);
                out.write(pageContent.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    @Override
    protected String getItemFileName(DigitalObject item) {
        return item.getId().substring(5) + ".txt";
    }
}
