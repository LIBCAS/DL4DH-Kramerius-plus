package cz.inqool.dl4dh.krameriusplus.core.job.report;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "kplus_step_run_report")
public class StepRunReport extends DomainObject {

    @ManyToOne
    private KrameriusJobInstance job;

    @NotNull
    private Long stepExecutionId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "stepRunReport")
    private Set<StepError> errors = new HashSet<>();
}
