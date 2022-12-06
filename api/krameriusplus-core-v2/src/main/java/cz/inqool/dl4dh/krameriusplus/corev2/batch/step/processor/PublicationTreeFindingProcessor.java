package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.ChainCreateWrapper;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PublicationTreeFindingProcessor implements ItemProcessor<String, ChainCreateWrapper> {

    private final PublicationStore publicationStore;

    @Autowired
    public PublicationTreeFindingProcessor(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Override
    public ChainCreateWrapper process(String item) throws Exception {
        ChainCreateWrapper chainCreateWrapper = new ChainCreateWrapper();
        chainCreateWrapper.setEnrichmentItemId(item);
        chainCreateWrapper.getPublicationIds().addAll(publicationStore.findPublicationTree(item));
        return chainCreateWrapper;
    }
}
