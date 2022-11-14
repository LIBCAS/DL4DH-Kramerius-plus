package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions;


import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class PersistedErrorStore extends DatedStore<PersistedError, QPersistedError> {

    public PersistedErrorStore() {
        super(PersistedError.class, QPersistedError.class);
    }
}
