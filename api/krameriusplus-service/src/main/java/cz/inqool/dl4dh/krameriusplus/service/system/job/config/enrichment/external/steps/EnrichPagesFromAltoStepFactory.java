package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.EnrichPagesFromAltoCompositeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EnrichPagesFromAltoStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesFromAltoCompositeProcessor enrichPagesFromAltoCompositeProcessor;



    @Autowired
    public EnrichPagesFromAltoStepFactory(EnrichPagesFromAltoCompositeProcessor enrichPagesFromAltoCompositeProcessor) {
        this.enrichPagesFromAltoCompositeProcessor = enrichPagesFromAltoCompositeProcessor;
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
        return "TEMPSTEPNAME";
    }
}
