package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.mods;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.publication.metadata.ModsWrapper;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPublicationModsProcessor implements ItemProcessor<Publication, Publication> {
    private final StreamProvider streamProvider;

    @Autowired
    public EnrichPublicationModsProcessor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public Publication process(@NonNull Publication item) throws Exception {
        ModsCollectionDefinition mods = streamProvider.getMods(item.getId());

        item.setModsMetadata(new ModsWrapper(mods).getTransformedMods());

        return item;
    }
}
