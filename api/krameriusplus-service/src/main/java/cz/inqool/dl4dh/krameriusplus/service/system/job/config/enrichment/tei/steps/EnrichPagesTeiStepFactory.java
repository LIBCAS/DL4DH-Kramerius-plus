package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PageMongoPersistentStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei.components.EnrichPagesTeiProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_TEI;

@Component
public class EnrichPagesTeiStepFactory extends PageMongoPersistentStepFactory {

    private final EnrichPagesTeiProcessor processor;

    @Autowired
    public EnrichPagesTeiStepFactory(EnrichPagesTeiProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_TEI;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }
}
