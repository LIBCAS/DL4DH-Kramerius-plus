package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.ParadataStepListener;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.EnrichPagesUDPipeProcessor;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_UD_PIPE;

@Component
public class EnrichPagesUDPipeStepFactory extends PageMongoFlowStepFactory {

    private final EnrichPagesUDPipeProcessor processor;

    private final ParadataStepListener stepListener;

    @Autowired
    public EnrichPagesUDPipeStepFactory(EnrichPagesUDPipeProcessor processor, ParadataStepListener stepListener) {
        this.processor = processor;
        this.stepListener = stepListener;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_UD_PIPE;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }

    @Override
    protected StepExecutionListener getStepExecutionListener() {
        return stepListener;
    }
}
