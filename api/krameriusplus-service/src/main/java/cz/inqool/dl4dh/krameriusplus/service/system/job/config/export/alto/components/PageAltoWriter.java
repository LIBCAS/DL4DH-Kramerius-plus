package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FileWriter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@StepScope
public class PageAltoWriter extends FileWriter<Page> {

    private final StreamProvider streamProvider;

    @Autowired
    public PageAltoWriter(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public void write(List<? extends Page> items) throws Exception {
        for (Page item : items) {
            String alto = streamProvider.getAltoString(item.getId());

            try (OutputStream out = getItemOutputStream(item)) {
                out.write(alto.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    @Override
    protected String getItemFileName(DigitalObject item) {
        return item.getId().substring(5) + ".xml";
    }
}
