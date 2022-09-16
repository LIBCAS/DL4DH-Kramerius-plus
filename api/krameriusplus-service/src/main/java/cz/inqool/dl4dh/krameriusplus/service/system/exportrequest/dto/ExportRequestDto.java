package cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.OwnedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.dto.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportRequestDto extends OwnedObjectDto {

    private String name;

    private JobPlanDto jobPlan;

    private BulkExportDto bulkExport;
}
