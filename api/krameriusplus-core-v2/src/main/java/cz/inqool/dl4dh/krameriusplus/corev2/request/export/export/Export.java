package cz.inqool.dl4dh.krameriusplus.corev2.request.export.export;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.bulk.BulkExport;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "kplus_export")
public class Export extends DatedObject {

    @NotNull
    private String publicationId;

    private String publicationTitle;

    @ManyToOne
    @NotNull
    private BulkExport bulkExport;

    @OneToOne
    private FileRef fileRef;

    @OneToOne
    @NotNull
    private KrameriusJobInstance exportJob;
}
