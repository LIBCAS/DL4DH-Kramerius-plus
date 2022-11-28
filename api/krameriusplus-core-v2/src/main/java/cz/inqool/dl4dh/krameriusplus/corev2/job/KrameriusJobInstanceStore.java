package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

@Repository
public class KrameriusJobInstanceStore extends DomainStore<KrameriusJobInstance, QKrameriusJobInstance> {

    public KrameriusJobInstanceStore() {
        super(KrameriusJobInstance.class, QKrameriusJobInstance.class);
    }
}
