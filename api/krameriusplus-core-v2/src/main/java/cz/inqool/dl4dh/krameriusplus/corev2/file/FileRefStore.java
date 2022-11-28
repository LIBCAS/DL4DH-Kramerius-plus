package cz.inqool.dl4dh.krameriusplus.corev2.file;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class FileRefStore extends DatedStore<FileRef, QFileRef> {

    public FileRefStore() {
        super(FileRef.class, QFileRef.class);
    }
}
