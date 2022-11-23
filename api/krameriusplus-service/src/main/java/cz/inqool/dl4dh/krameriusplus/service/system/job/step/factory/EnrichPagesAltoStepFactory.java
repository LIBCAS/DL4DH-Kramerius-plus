package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.EnrichPagesFromAltoCompositeProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_ALTO;

@Component
public class EnrichPagesAltoStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesFromAltoCompositeProcessor enrichPagesFromAltoCompositeProcessor;

    @Autowired
    public EnrichPagesAltoStepFactory(EnrichPagesFromAltoCompositeProcessor enrichPagesFromAltoCompositeProcessor) {
        this.enrichPagesFromAltoCompositeProcessor = enrichPagesFromAltoCompositeProcessor;
    }

    @Bean(ENRICH_PAGES_ALTO)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
       return enrichPagesFromAltoCompositeProcessor;
    }

    @Override
    protected int getChunkSize() {
        return 10;
    }


    @Override
    protected String getStepName() {
        return ENRICH_PAGES_ALTO;
    }
}
