package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.CleanUpPagesProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CLEAN_UP_PAGES;

@Component
public class CleanUpPagesStepFactory extends PageMongoFlowStepFactory {

    private final CleanUpPagesProcessor processor;

    @Autowired
    public CleanUpPagesStepFactory(CleanUpPagesProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return CLEAN_UP_PAGES;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }

}
