package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.AltoStringWrappedPage;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessenger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class AltoTransformingProcessor implements ItemProcessor<Page, AltoStringWrappedPage> {

    private KrameriusMessenger krameriusMessenger;

    @Override
    public AltoStringWrappedPage process(Page item) throws Exception {
        return new AltoStringWrappedPage(item, krameriusMessenger.getAltoString(item.getId()));
    }

    @Autowired
    public void setKrameriusMessenger(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }
}
