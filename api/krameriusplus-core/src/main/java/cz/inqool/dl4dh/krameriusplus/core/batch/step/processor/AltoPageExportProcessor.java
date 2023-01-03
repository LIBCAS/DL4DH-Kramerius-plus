package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class AltoPageExportProcessor implements ItemProcessor<Page, DigitalObjectExport> {

    private KrameriusMessenger krameriusMessenger;

    @Override
    public DigitalObjectExport process(Page item) throws Exception {
        return new DigitalObjectExport(
                item,
                krameriusMessenger.getAltoString(item.getId()),
                item.getId().substring(5) + ".xml");
    }

    @Autowired
    public void setKrameriusMessenger(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }
}
