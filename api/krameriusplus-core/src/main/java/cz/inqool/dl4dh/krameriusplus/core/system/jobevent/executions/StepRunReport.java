package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class StepRunReport extends DomainObject {

    @ManyToOne
    private JobEvent jobEvent;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "stepRunReport")
    private Set<PersistedError> persistedErrors = new HashSet<>();

    public PersistedError addError(Throwable e, Object causingObject) {
        PersistedError persistedError = new PersistedError();
        if (causingObject instanceof Page) {
            Page page = ((Page) causingObject);
            persistedError.setPageId(page.getId());
            persistedError.setPageIndex(page.getIndex());
        }

        persistedError.setStepRunReport(this);
        persistedError.setShortMessage(e.getMessage());
        persistedErrors.add(persistedError);
        return persistedError;
    }
}
