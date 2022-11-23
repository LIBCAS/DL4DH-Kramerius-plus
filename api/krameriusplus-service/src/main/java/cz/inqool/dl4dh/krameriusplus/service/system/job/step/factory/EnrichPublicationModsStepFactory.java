package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.EnrichPublicationModsProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PUBLICATION_MODS;


@Component
public class EnrichPublicationModsStepFactory extends PublicationMongoFlowStepFactory {

    private EnrichPublicationModsProcessor processor;

    @Bean(ENRICH_PUBLICATION_MODS)
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
        return ENRICH_PUBLICATION_MODS;
    }

    @Autowired
    public void setProcessor(EnrichPublicationModsProcessor processor) {
        this.processor = processor;
    }
}
