package cz.inqool.dl4dh.krameriusplus.core.job.report;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class StepRunReportStore extends DomainStore<StepRunReport, QStepRunReport> {

    public StepRunReportStore(EntityManager em) {
        super(StepRunReport.class, QStepRunReport.class, em);
    }
}
