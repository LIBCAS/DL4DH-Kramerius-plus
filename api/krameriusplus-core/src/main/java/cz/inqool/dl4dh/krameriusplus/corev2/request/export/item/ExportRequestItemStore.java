package cz.inqool.dl4dh.krameriusplus.corev2.request.export.item;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ExportRequestItemStore extends DomainStore<ExportRequestItem, QExportRequestItem> {

    public ExportRequestItemStore(EntityManager em) {
        super(ExportRequestItem.class, QExportRequestItem.class, em);
    }
}
