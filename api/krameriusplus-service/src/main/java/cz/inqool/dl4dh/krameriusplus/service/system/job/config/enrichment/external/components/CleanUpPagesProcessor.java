package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CleanUpPagesProcessor implements ItemProcessor<Page, Page> {

    @Override
    public Page process(Page item) {
        item.setAltoLayout(null);
        item.setContent(null);

        return item;
    }
}
