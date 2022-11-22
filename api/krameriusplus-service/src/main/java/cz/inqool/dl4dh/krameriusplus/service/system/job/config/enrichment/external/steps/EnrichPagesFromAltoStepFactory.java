package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.ErrorPersistingSkipPolicy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.EnrichPagesFromAltoCompositeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_ALTO;

@Component
@Slf4j
public class EnrichPagesFromAltoStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesFromAltoCompositeProcessor enrichPagesFromAltoCompositeProcessor;

    @Autowired
    public EnrichPagesFromAltoStepFactory(EnrichPagesFromAltoCompositeProcessor enrichPagesFromAltoCompositeProcessor, ErrorPersistingSkipPolicy errorPersistingSkipPolicy) {
        this.enrichPagesFromAltoCompositeProcessor = enrichPagesFromAltoCompositeProcessor;
        this.errorPersistingSkipPolicy = errorPersistingSkipPolicy;
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
