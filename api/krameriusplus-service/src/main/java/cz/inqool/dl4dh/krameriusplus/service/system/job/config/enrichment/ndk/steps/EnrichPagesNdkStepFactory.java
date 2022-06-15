package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components.EnrichPagesNdkProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_NDK;

@Component
public class EnrichPagesNdkStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesNdkProcessor processor;

    @Autowired
    public EnrichPagesNdkStepFactory(EnrichPagesNdkProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_NDK;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }
}
