package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.internalpart;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.DomainStore;
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
