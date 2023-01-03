package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.AltoWrappedPage;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.enricher.lindat.NameTagEnricher;
import cz.inqool.dl4dh.krameriusplus.core.enricher.lindat.NameTagResponse;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class NameTagProcessor implements ItemProcessor<AltoWrappedPage, AltoWrappedPage> {

    private final NameTagEnricher nameTagEnricher;

    private boolean isParadataExtracted = false;

    private final PublicationStore publicationStore;

    @Autowired
    public NameTagProcessor(NameTagEnricher nameTagEnricher, PublicationStore publicationStore) {
        this.nameTagEnricher = nameTagEnricher;
        this.publicationStore = publicationStore;
    }

    @Override
    public AltoWrappedPage process(@NonNull AltoWrappedPage item) {
        NameTagResponse response = nameTagEnricher.enrichPageTokens(item.getPage().getTokens());
        item.getPage().setNameTagMetadata(response.getMetadata());

        if (!isParadataExtracted && response.getParadata() != null) {
            Publication publication = publicationStore.findById(item.getPage().getParentId()).orElseThrow(() -> new IllegalStateException("Page should always have a parent in db"));
            publication.getParadata().put(response.getParadata().getExternalSystem(), response.getParadata());
            publicationStore.save(publication);
            isParadataExtracted = true;
        }

        return item;
    }
}
