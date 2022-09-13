package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExport;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ExportRequest extends OwnedObject {

    @OneToOne
    private JobPlan jobPlan;

    @OneToOne
    private BulkExport bulkExport;

    @JoinTable(name = "export_request_export",
            joinColumns = @JoinColumn(name = "export_request_id"),
            inverseJoinColumns = @JoinColumn(name = "export_id"))
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Export> exports = new HashSet<>();
}
