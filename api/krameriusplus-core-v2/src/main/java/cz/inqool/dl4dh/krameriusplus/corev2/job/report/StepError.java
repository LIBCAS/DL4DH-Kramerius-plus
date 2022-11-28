package cz.inqool.dl4dh.krameriusplus.corev2.job.report;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "kplus_step_error")
public class StepError extends DomainObject {

    @ManyToOne
    private StepRunReport stepRunReport;

    private String shortMessage;

    @Lob
    private String stackTrace;
}
