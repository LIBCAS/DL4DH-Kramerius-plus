package cz.inqool.dl4dh.krameriusplus.core.file;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class FileRefStore extends DatedStore<FileRef, QFileRef> {

    public FileRefStore(EntityManager em) {
        super(FileRef.class, QFileRef.class, em);
    }
}
