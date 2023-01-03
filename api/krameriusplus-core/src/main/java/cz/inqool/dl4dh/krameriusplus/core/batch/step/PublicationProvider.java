package cz.inqool.dl4dh.krameriusplus.core.batch.step;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class providing Publication data for exports. Delegates to {@link PublicationStore} or {@link KrameriusMessenger}
 * if publication is not found in local DB
 */
@Component
public class PublicationProvider {

    private PublicationStore publicationStore;

    private KrameriusMessenger krameriusMessenger;

    public Publication find(String publicationId) {
        return publicationStore.findById(publicationId)
                .orElseGet(() -> {
                    DigitalObject digitalObject = krameriusMessenger.getDigitalObject(publicationId);
                    Utils.isTrue(digitalObject instanceof Publication, () -> new MissingObjectException(Publication.class, publicationId));

                    return (Publication) digitalObject;
                });
    }

    public List<Publication> findChildren(String parentId) {
        Optional<Publication> publication = publicationStore.findById(parentId);
        if (publication.isPresent()) {
            return publicationStore.findAllChildren(parentId);
        } else {
            return krameriusMessenger.getDigitalObjectsForParent(parentId).stream()
                    .filter(obj -> obj instanceof Publication)
                    .map(obj -> (Publication) obj)
                    .collect(Collectors.toList());
        }
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setKrameriusMessenger(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }
}
