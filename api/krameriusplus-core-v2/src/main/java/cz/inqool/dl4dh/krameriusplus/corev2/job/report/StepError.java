package cz.inqool.dl4dh.krameriusplus.corev2.job.report;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "kplus_step_error")
public class StepError extends DomainObject {

    @ManyToOne
    private StepRunReport stepRunReport;

    private String exitCode;

    private String shortMessage;

    @Lob
    private String stackTrace;

    @Column(name = "error_order")
    private Long order;
}
