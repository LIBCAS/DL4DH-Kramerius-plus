package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationStore extends CrudRepository<Publication, String>, CustomPublicationStore {
}
