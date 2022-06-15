package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PublicationMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components.PreparePublicationNdkProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PUBLICATION_NDK;

@Component
public class PreparePublicationNdkStepFactory extends PublicationMongoFlowStepFactory {

    private final PreparePublicationNdkProcessor processor;

    @Autowired
    public PreparePublicationNdkStepFactory(PreparePublicationNdkProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return PREPARE_PUBLICATION_NDK;
    }

    @Override
    protected ItemProcessor<Publication, Publication> getItemProcessor() {
        return processor;
    }
}
