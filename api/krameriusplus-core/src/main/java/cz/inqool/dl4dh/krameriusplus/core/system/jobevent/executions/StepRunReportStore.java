package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DomainStore;
import org.springframework.stereotype.Repository;

@Repository
public class StepRunReportStore extends DomainStore<StepRunReport, QStepRunReport> {

    public StepRunReportStore() {
        super(StepRunReport.class, QStepRunReport.class);
    }
}
