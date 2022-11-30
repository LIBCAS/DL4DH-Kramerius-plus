package cz.inqool.dl4dh.krameriusplus.corev2.request.export.bulk;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kplus_bulk_export")
public class BulkExport extends DatedObject {

    @OneToOne
    private FileRef fileRef;

    @OneToMany(mappedBy = "bulkExport", orphanRemoval = true)
    private List<Export> exports = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExportFormat format;

    @OneToOne
    @NotNull
    private KrameriusJobInstance mergeJob;
}
