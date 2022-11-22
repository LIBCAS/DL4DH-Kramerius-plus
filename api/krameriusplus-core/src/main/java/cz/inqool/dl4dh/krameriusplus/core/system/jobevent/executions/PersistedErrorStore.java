package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions;


import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DomainStore;
import org.springframework.stereotype.Repository;

@Repository
public class PersistedErrorStore extends DomainStore<PersistedError, QPersistedError> {

    public PersistedErrorStore() {
        super(PersistedError.class, QPersistedError.class);
    }
}
