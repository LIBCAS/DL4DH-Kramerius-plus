package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.export.ExportJobConfig;
import cz.inqool.dl4dh.krameriusplus.corev2.request.Request;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_request")
public class ExportRequest extends Request {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ExportJobConfig config;

    @OneToMany(mappedBy = "exportRequest")
    private List<ExportRequestItem> items = new ArrayList<>();

    @OneToOne
    @NotNull
    private KrameriusJobInstance createRequestJob;

    @Embedded
    @NotNull
    private BulkExport bulkExport;
}
