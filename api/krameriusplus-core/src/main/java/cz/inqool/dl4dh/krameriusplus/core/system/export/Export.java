package cz.inqool.dl4dh.krameriusplus.core.system.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Entity
public class Export extends DatedObject {

    private String publicationId;

    private String publicationTitle;

    @OneToOne
    private FileRef fileRef;

    @OneToOne
    private JobEvent jobEvent;
}
