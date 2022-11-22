package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.CustomSkipPolicy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei.components.EnrichPagesTeiProcessor;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_TEI;

@Component
public class EnrichPagesTeiStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesTeiProcessor processor;

    private final CustomSkipPolicy customSkipPolicy;

    @Autowired
    public EnrichPagesTeiStepFactory(EnrichPagesTeiProcessor processor, CustomSkipPolicy customSkipPolicy) {
        this.processor = processor;
        this.customSkipPolicy = customSkipPolicy;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_TEI;
    }

    @Override
    protected SkipPolicy getSkipPolicy() {
        return customSkipPolicy;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }
}
