package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExport;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
public class ExportRequest extends OwnedObject {

    private String name;

    @OneToOne
    private JobPlan jobPlan;

    @OneToOne
    private BulkExport bulkExport;
}
