package cz.inqool.dl4dh.krameriusplus.core.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @deprecated SinglePublicationReader is not useful. ItemReader is meant to be used when multiple items are read,
 * processed and written, so that the state of reading can be stored in ExecutionContext for restarting.
 * Otherwise just use a Tasklet instead.
 */
@Component
@StepScope
@Deprecated
public class SinglePublicationReader implements ItemReader<Publication> {

    @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}")
    private String rootId;

    private PublicationStore publicationStore;

    private boolean isRead = false;

    @Override
    public Publication read() {
        Utils.notNull(rootId, () -> new IllegalStateException("ItemReader used without PUBLICATION_ID key in JobParameters."));

        if (isRead) {
            return null;
        } else {
            isRead = true;
            Optional<Publication> result = publicationStore.findById(rootId);
            Utils.isTrue(result.isPresent(), () -> new IllegalArgumentException("Publication with ID: " + rootId + " not found in MongoDB."));

            return result.get();
        }
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }
}
