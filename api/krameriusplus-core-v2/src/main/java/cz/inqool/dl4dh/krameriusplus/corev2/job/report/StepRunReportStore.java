package cz.inqool.dl4dh.krameriusplus.corev2.job.report;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

@Repository
public class StepRunReportStore extends DomainStore<StepRunReport, QStepRunReport> {

    public StepRunReportStore() {
        super(StepRunReport.class, QStepRunReport.class);
    }
}
