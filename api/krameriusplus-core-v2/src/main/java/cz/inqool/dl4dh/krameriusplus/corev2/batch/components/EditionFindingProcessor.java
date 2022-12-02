package cz.inqool.dl4dh.krameriusplus.corev2.batch.components;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.CustomPublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class EditionFindingProcessor implements ItemProcessor<String, List<String>> {

    private final CustomPublicationStore customPublicationStore;

    @Autowired
    public EditionFindingProcessor(CustomPublicationStore customPublicationStore) {
        this.customPublicationStore = customPublicationStore;
    }

    @Override
    public List<String> process(String item) throws Exception {
        return customPublicationStore.findAllEditions(item);
    }
}
