package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.AltoWrappedPage;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.lindat.UDPipeEnricher;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.lindat.UDPipeResponse;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class UDPipeProcessor implements ItemProcessor<AltoWrappedPage, AltoWrappedPage> {

    private final UDPipeEnricher udPipeEnricher;

    private final PublicationStore publicationStore;

    private boolean isParadataExtracted = false;

    @Autowired
    public UDPipeProcessor(UDPipeEnricher udPipeEnricher, PublicationStore publicationStore) {
        this.udPipeEnricher = udPipeEnricher;
        this.publicationStore = publicationStore;
    }

    @Override
    public AltoWrappedPage process(@NonNull AltoWrappedPage item) {
        UDPipeResponse response = udPipeEnricher.processPageContent(item.getContent());
        item.getPage().setTokens(response.getTokens());

        if (!isParadataExtracted && response.getParadata() != null) {
            Publication publication = publicationStore.findById(item.getPage().getParentId())
                    .orElseThrow(() -> new IllegalStateException("Page " + item.getPage().getId() + " has no parent publication in DB, " +
                            "could not save paradata."));
            publication.getParadata().put(response.getParadata().getExternalSystem(), response.getParadata());
            publicationStore.save(publication);
            isParadataExtracted = true;
        }

        return item;
    }
}
