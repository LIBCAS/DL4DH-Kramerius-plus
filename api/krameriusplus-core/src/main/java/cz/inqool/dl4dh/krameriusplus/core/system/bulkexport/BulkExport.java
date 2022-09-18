package cz.inqool.dl4dh.krameriusplus.core.system.bulkexport;


import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class BulkExport extends DatedObject {

    @OneToOne
    private FileRef fileRef;

    @JoinTable(name = "bulk_export_exports",
            joinColumns = @JoinColumn(name = "bulk_export_id"),
            inverseJoinColumns = @JoinColumn(name = "export_id"))
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Export> exports = new HashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExportFormat format;
}
