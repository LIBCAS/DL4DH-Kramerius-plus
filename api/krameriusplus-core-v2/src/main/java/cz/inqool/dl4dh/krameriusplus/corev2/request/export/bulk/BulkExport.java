package cz.inqool.dl4dh.krameriusplus.corev2.request.export.bulk;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kplus_bulk_export")
public class BulkExport extends DatedObject {

    @OneToOne
    private FileRef fileRef;

    @OneToMany
    private List<Export> exports = new ArrayList<>();

    private ExportFormat format;

    @OneToOne
    private KrameriusJobInstance mergeJob;
}
