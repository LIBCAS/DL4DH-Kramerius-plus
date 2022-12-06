package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class PublicationTreeFindingProcessor implements ItemProcessor<String, List<String>> {

    private final PublicationStore publicationStore;

    @Autowired
    public PublicationTreeFindingProcessor(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Override
    public List<String> process(String item) throws Exception {
        return publicationStore.findAllEditions(item);
    }
}
