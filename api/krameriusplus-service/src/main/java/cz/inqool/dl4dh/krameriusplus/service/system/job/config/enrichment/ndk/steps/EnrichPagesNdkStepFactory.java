package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components.EnrichPagesNdkProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components.PreparePagesNdkProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_NDK;

@Component
public class EnrichPagesNdkStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesNdkProcessor enrichPagesNdkProcessor;

    private final PreparePagesNdkProcessor preparePagesNdkProcessor;

    @Autowired
    public EnrichPagesNdkStepFactory(EnrichPagesNdkProcessor enrichPagesNdkProcessor, PreparePagesNdkProcessor preparePagesNdkProcessor) {
        this.enrichPagesNdkProcessor = enrichPagesNdkProcessor;
        this.preparePagesNdkProcessor = preparePagesNdkProcessor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_NDK;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return new CompositeItemProcessorBuilder<Page, Page>()
                .delegates(preparePagesNdkProcessor, enrichPagesNdkProcessor)
                .build();
    }
}
