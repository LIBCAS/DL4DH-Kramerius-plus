package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class StepRunReportStore extends DatedStore<StepRunReport, QStepRunReport> {

    public StepRunReportStore() {
        super(StepRunReport.class, QStepRunReport.class);
    }
}
