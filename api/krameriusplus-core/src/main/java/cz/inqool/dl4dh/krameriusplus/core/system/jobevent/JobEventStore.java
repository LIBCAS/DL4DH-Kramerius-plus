package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class JobEventStore extends DatedStore<JobEvent, QJobEvent> {

    public JobEventStore() {
        super(JobEvent.class, QJobEvent.class);
    }
}
