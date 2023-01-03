package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessenger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class TextPageExportProcessor implements ItemProcessor<Page, DigitalObjectExport> {

    private KrameriusMessenger krameriusMessenger;

    @Override
    public DigitalObjectExport process(Page item) throws Exception {
        return new DigitalObjectExport(
                item,
                krameriusMessenger.getOcr(item.getId()),
                item.getId().substring(5) + ".txt"
        );
    }

    @Autowired
    public void setKrameriusMessenger(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }
}
