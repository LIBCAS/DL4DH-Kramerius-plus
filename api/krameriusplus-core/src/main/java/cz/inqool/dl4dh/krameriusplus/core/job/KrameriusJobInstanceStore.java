package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class KrameriusJobInstanceStore extends DomainStore<KrameriusJobInstance, QKrameriusJobInstance> {

    public KrameriusJobInstanceStore(EntityManager em) {
        super(KrameriusJobInstance.class, QKrameriusJobInstance.class, em);
    }
}
