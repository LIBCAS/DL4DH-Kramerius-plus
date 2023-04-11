package cz.inqool.dl4dh.krameriusplus.core.request.export.request;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceView;
import cz.inqool.dl4dh.krameriusplus.core.job.config.export.ExportJobConfigView;
import cz.inqool.dl4dh.krameriusplus.core.request.Request;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_request")
public class ExportRequestView extends Request {

    @OneToOne(mappedBy = "exportRequest")
    private ExportJobConfigView config;

    @OneToOne
    private KrameriusJobInstanceView createRequestJob;

    @Embedded
    @NotNull
    private BulkExport bulkExport;
}
