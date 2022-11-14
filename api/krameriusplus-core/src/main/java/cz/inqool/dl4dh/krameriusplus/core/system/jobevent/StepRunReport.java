package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Getter
@Setter
@Entity
public class StepRunReport extends DatedObject {

    @ManyToOne
    private JobEventRunReport jobEventRunReport;

    @OneToMany
    private Set<PersistedError> persistedErrorSet;
}
