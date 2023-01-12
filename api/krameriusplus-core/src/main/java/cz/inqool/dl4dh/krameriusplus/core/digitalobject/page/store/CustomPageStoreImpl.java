package cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.domain.document.DomainDocumentStore;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class CustomPageStoreImpl extends DomainDocumentStore<Page> implements CustomPageStore {

    public CustomPageStoreImpl(MongoOperations mongoOperations) {
        super(mongoOperations, Page.class);
    }

}
