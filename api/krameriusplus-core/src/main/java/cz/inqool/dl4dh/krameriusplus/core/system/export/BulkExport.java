package cz.inqool.dl4dh.krameriusplus.core.system.export;


import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
public class BulkExport extends DatedObject {

    @OneToOne
    private FileRef fileRef;

    // TODO: zmenit naviazanie na plan
    @OneToOne
    private JobEvent jobEvent;
}
