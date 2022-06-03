package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PageMongoPersistentStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.EnrichPagesUDPipeProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobStep.ENRICH_PAGES_UD_PIPE;

@Component
public class EnrichPagesUDPipe extends PageMongoPersistentStepFactory {

    private final EnrichPagesUDPipeProcessor processor;

    @Autowired
    public EnrichPagesUDPipe(EnrichPagesUDPipeProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_UD_PIPE;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }
}
