package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.dao.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class PublicationStore extends DomainStore<Publication> {

    @Autowired
    public PublicationStore(MongoOperations mongoOperations) {
        super(Publication.class, mongoOperations);
    }
}
