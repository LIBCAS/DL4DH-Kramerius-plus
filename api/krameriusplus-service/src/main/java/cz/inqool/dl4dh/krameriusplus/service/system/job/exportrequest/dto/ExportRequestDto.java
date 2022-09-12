package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.OwnedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportRequestDto extends OwnedObjectDto {

    private BulkExportDto bulkExportDto;
}
