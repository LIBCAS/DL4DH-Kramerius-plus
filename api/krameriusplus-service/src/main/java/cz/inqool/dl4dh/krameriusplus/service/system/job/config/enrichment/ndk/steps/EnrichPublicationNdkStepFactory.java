package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PublicationMongoPersistentStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components.EnrichPublicationNdkProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PUBLICATION_NDK;

@Component
public class EnrichPublicationNdkStepFactory extends PublicationMongoPersistentStepFactory {

    private final EnrichPublicationNdkProcessor processor;

    @Autowired
    public EnrichPublicationNdkStepFactory(EnrichPublicationNdkProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PUBLICATION_NDK;
    }

    @Override
    protected ItemProcessor<Publication, Publication> getItemProcessor() {
        return processor;
    }
}
