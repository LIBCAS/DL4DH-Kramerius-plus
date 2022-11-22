package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class PersistedError extends DomainObject {

    @ManyToOne
    private StepRunReport stepRunReport;

    private String shortMessage;

    private String pageId;

    private Integer pageIndex;
}
