package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.ExportJobConfig;
import cz.inqool.dl4dh.krameriusplus.corev2.request.Request;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.bulk.BulkExport;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_request")
public class ExportRequest extends Request {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ExportJobConfig config;

    @OneToOne
    private BulkExport bulkExport;

    @OneToOne
    private KrameriusJobInstance createRequestJob;
}
