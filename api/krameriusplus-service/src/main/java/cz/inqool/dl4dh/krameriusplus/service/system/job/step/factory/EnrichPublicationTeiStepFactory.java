package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.EnrichPublicationTeiProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PUBLICATION_TEI;

@Component
public class EnrichPublicationTeiStepFactory extends PublicationMongoFlowStepFactory {

    private final EnrichPublicationTeiProcessor processor;

    @Autowired
    public EnrichPublicationTeiStepFactory(EnrichPublicationTeiProcessor processor) {
        this.processor = processor;
    }

    @Bean(ENRICH_PUBLICATION_TEI)
    @Override
    public Step build() {
        return super.build();
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
