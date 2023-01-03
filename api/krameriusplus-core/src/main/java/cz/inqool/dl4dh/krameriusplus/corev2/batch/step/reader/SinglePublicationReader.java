package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.isTrue;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

/**
 * @deprecated SinglePublicationReader is not useful. ItemReader is meant to be used when multiple items are read,
 * processed and written, so that the state of reading can be stored in ExecutionContext for restarting.
 * Otherwise just use a Tasklet instead.
 */
@Component
@StepScope
@Deprecated
public class SinglePublicationReader implements ItemReader<Publication> {

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
    private String rootId;

    private PublicationStore publicationStore;

    private boolean isRead = false;

    @Override
    public Publication read() {
        notNull(rootId, () -> new IllegalStateException("ItemReader used without PUBLICATION_ID key in JobParameters."));

        if (isRead) {
            return null;
        } else {
            isRead = true;
            Optional<Publication> result = publicationStore.findById(rootId);
            isTrue(result.isPresent(), () -> new IllegalArgumentException("Publication with ID: " + rootId + " not found in MongoDB."));

            return result.get();
        }
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }
}
