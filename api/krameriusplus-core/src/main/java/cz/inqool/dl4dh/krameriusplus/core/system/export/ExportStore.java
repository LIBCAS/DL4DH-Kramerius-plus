package cz.inqool.dl4dh.krameriusplus.core.system.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class ExportStore extends DomainStore<Export> {

    @Autowired
    public ExportStore(MongoOperations mongoOperations) {
        super(Export.class, mongoOperations);
    }
}
