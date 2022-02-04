package cz.inqool.dl4dh.krameriusplus.domain.entity.file;

import cz.inqool.dl4dh.krameriusplus.domain.repo.DomainStore;
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
