package cz.inqool.dl4dh.krameriusplus.core.system.export;


import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class MergedExport extends DatedObject {

    @OneToOne
    private FileRef fileRef;

    @OneToOne
    private JobEvent jobEvent;

    @JoinTable(name = "merged_export_export",
            joinColumns = @JoinColumn(name = "merged_export_id"),
            inverseJoinColumns = @JoinColumn(name = "export_id"))
    @OneToMany
    private Set<Export> exports = new HashSet<>();
}
