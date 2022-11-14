package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@Getter
@Setter
@Entity
public class JobEventRunReport extends DatedObject {

    @OneToOne
    private JobEvent jobEvent;

    @OneToMany
    private Set<StepRunReport> stepRunReport;
}
