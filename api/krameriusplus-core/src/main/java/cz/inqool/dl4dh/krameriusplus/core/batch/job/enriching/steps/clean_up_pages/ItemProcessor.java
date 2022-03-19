package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.clean_up_pages;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@StepScope
@Named(Steps.CleanUpPages.PROCESSOR_NAME)
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Page, Page> {

    @Override
    public Page process(Page item) throws Exception {
        item.setAltoLayout(null);
        item.setContent(null);

        return item;
    }
}
