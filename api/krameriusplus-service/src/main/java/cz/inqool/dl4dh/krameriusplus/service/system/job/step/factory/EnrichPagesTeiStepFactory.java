package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.EnrichPagesTeiProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_TEI;

@Component
public class EnrichPagesTeiStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesTeiProcessor processor;

    @Autowired
    public EnrichPagesTeiStepFactory(EnrichPagesTeiProcessor processor) {
        this.processor = processor;
    }

    @Bean(ENRICH_PAGES_TEI)
    @Override
    public Step build() {
        return super.build();
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
