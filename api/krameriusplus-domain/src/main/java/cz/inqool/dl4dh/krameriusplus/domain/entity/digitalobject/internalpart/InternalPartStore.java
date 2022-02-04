package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart;

import cz.inqool.dl4dh.krameriusplus.domain.repo.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class InternalPartStore extends DomainStore<InternalPart> {

    @Autowired
    public InternalPartStore(MongoOperations mongoOperations) {
        super(InternalPart.class, mongoOperations);
    }
}
