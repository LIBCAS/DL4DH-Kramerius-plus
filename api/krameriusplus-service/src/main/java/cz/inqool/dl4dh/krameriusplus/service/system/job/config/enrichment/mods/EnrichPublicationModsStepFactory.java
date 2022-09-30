package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.mods;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PublicationMongoFlowStepFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PUBLICATION_MODS;


@Component
public class EnrichPublicationModsStepFactory extends PublicationMongoFlowStepFactory {

    private EnrichPublicationModsProcessor processor;

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
