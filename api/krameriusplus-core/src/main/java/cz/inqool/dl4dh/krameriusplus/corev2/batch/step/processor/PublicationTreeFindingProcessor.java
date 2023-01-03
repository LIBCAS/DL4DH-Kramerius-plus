package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.ChainCreateWrapper;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

        Publication rootNode = publicationStore.findPublicationTree(item);
        chainCreateWrapper.setPublications(extractData(rootNode));

        return chainCreateWrapper;
    }

    /**
     * Recursively extract main data from publication and returns them in a flat list structure
     * @param publication publication to extract data from, including it's children
     * @return list of publication data
     */
    private List<ChainCreateWrapper.PublicationData> extractData(Publication publication) {
        List<ChainCreateWrapper.PublicationData> result = new ArrayList<>();

        ChainCreateWrapper.PublicationData currentData = new ChainCreateWrapper.PublicationData();
        currentData.setPublicationId(publication.getId());
        currentData.setPublicationTitle(publication.getTitle());
        currentData.setModel(publication.getModel());
        result.add(currentData);

        for (Publication child : publication.getChildren()) {
            result.addAll(extractData(child));
        }

        return result;
    }
}
