package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ExportJobConfigStore extends DomainStore<ExportJobConfig, QExportJobConfig> {

    public ExportJobConfigStore(EntityManager em) {
        super(ExportJobConfig.class, QExportJobConfig.class, em);
    }
}
