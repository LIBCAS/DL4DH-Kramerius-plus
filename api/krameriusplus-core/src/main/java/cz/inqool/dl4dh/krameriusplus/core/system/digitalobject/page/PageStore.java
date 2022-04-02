package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.dao.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class PageStore extends DomainStore<Page> {

    @Autowired
    public PageStore(MongoOperations mongoOperations) {
        super(Page.class, mongoOperations);
    }
}
