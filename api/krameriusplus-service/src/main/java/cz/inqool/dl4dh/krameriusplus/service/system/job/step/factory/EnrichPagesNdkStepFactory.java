package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.EnrichPagesNdkCompositeProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_NDK;

@Component
public class EnrichPagesNdkStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesNdkCompositeProcessor enrichPagesNdkCompositeProcessor;

    @Autowired
    public EnrichPagesNdkStepFactory(EnrichPagesNdkCompositeProcessor enrichPagesNdkCompositeProcessor) {
        this.enrichPagesNdkCompositeProcessor = enrichPagesNdkCompositeProcessor;
    }

    @Bean(ENRICH_PAGES_NDK)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_NDK;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return enrichPagesNdkCompositeProcessor;
    }


}
