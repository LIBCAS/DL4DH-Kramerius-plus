package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PublicationMongoPersistentStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei.components.EnrichPublicationTeiProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobStep.ENRICH_PUBLICATION_TEI;

@Component
public class EnrichPublicationTeiStepFactory extends PublicationMongoPersistentStepFactory {

    private final EnrichPublicationTeiProcessor processor;

    @Autowired
    public EnrichPublicationTeiStepFactory(EnrichPublicationTeiProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected ItemProcessor<Publication, Publication> getItemProcessor() {
        return processor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PUBLICATION_TEI;
    }
}
