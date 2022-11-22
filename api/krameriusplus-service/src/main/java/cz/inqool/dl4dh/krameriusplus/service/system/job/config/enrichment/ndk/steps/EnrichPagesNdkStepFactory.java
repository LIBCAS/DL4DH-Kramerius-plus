package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.CustomSkipPolicy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components.EnrichPagesNdkCompositeProcessor;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_NDK;

@Component
public class EnrichPagesNdkStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesNdkCompositeProcessor enrichPagesNdkCompositeProcessor;

    private final CustomSkipPolicy customSkipPolicy;

    @Autowired
    public EnrichPagesNdkStepFactory(EnrichPagesNdkCompositeProcessor enrichPagesNdkCompositeProcessor, CustomSkipPolicy customSkipPolicy) {
        this.enrichPagesNdkCompositeProcessor = enrichPagesNdkCompositeProcessor;
        this.customSkipPolicy = customSkipPolicy;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_NDK;
    }

    @Override
    protected SkipPolicy getSkipPolicy() {
        return customSkipPolicy;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return enrichPagesNdkCompositeProcessor;
    }


}
