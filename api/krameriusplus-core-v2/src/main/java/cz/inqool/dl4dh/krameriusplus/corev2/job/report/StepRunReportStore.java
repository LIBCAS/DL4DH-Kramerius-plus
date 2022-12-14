package cz.inqool.dl4dh.krameriusplus.corev2.job.report;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class StepRunReportStore extends DomainStore<StepRunReport, QStepRunReport> {

    public StepRunReportStore(EntityManager em) {
        super(StepRunReport.class, QStepRunReport.class, em);
    }
}
