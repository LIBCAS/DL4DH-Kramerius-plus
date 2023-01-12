package cz.inqool.dl4dh.krameriusplus.core.request.export.request;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.config.export.ExportJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.request.Request;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_request")
public class ExportRequest extends Request {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "request")
    private ExportJobConfig config;

    @OneToMany(mappedBy = "exportRequest", fetch = FetchType.EAGER)
    private Set<ExportRequestItem> items = new HashSet<>();

    @OneToOne
    @NotNull
    private KrameriusJobInstance createRequestJob;

    @Embedded
    @NotNull
    private BulkExport bulkExport;

    public void setConfig(ExportJobConfig jobConfig) {
        config = jobConfig;
        jobConfig.setRequest(this);
    }
}
