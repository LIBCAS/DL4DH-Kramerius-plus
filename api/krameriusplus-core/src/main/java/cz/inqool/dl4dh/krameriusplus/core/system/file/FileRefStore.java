package cz.inqool.dl4dh.krameriusplus.core.system.file;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class FileRefStore extends DatedStore<FileRef, QFileRef> {

    public FileRefStore() {
        super(FileRef.class, QFileRef.class);
    }
}
