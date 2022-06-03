package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PageMongoPersistentStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components.PreparePagesNdkProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PAGES_NDK;

@Component
public class PreparePagesNdkStepFactory extends PageMongoPersistentStepFactory {

    private final PreparePagesNdkProcessor processor;

    @Autowired
    public PreparePagesNdkStepFactory(PreparePagesNdkProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return PREPARE_PAGES_NDK;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }
}

