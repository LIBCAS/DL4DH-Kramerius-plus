package cz.inqool.dl4dh.krameriusplus.core.system.file;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class FileRefStore extends DomainStore<FileRef> {

    @Autowired
    public FileRefStore(MongoOperations mongoOperations) {
        super(FileRef.class, mongoOperations);
    }
}
