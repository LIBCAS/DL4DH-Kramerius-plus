package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_publication_mods;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.metadata.ModsWrapper;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Named(value = Steps.EnrichPublicationMods.PROCESSOR_NAME)
@StepScope
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Publication, Publication> {

    private final StreamProvider streamProvider;

    @Autowired
    ItemProcessor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public Publication process(Publication publication) {
        ModsCollectionDefinition mods = streamProvider.getMods(publication.getId());

        publication.setModsMetadata(new ModsWrapper(mods).getTransformedMods());

        return publication;
    }
}
