package cz.inqool.dl4dh.krameriusplus.corev2.job.config.export;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

@Repository
public class ExportJobConfigStore extends DomainStore<ExportJobConfig, QExportJobConfig> {

    public ExportJobConfigStore() {
        super(ExportJobConfig.class, QExportJobConfig.class);
    }
}
