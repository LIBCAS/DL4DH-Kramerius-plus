package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString
@Entity
public class PersistedError extends DatedObject {

    @ManyToOne
    private StepRunReport stepRunReport;

    private String shortMessage;

    private String pageId;

    private String pageIndex;
}
