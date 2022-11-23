package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class StepRunReport extends DomainObject {

    @ManyToOne
    private JobEvent jobEvent;

    @NotNull
    private Long stepExecutionId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "stepRunReport")
    private Set<PersistedError> persistedErrors = new HashSet<>();

    public void addError(Throwable e) {
        PersistedError persistedError = new PersistedError();
        persistedError.setStepRunReport(this);
        persistedError.setShortMessage(e.getMessage());
        persistedError.setStackTrace(ExceptionUtils.getStackTrace(ExceptionUtils.getRootCause(e)));

        persistedErrors.add(persistedError);
    }
}
