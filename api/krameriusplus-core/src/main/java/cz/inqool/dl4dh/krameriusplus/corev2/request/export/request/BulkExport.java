package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportState;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Embeddable
public class BulkExport {

    @OneToOne
    private KrameriusJobInstance mergeJob;

    @OneToOne
    private FileRef file;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExportState state;

}
