package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
public class DownloadDigitalObjectProcessor implements ItemProcessor<DigitalObject, DigitalObject> {

    private final PublicationStore publicationStore;

    private final List<String> processedItems = new ArrayList<>();

    @Autowired
    public DownloadDigitalObjectProcessor(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Override
    public DigitalObject process(DigitalObject item) throws Exception {
        if (item instanceof Publication) {
            String parentId = item.getParentId();
            if (parentId == null || (!publicationStore.existsById(parentId)) && !processedItems.contains(parentId)) {
                ((Publication) item).setRootEnrichment(true);
            }

            processedItems.add(item.getId());
        }

        return item;
    }
}
