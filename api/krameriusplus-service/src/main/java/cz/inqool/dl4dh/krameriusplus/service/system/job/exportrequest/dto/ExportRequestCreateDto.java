package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportCreateDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExportRequestCreateDto extends DatedObjectCreateDto {

    private BulkExportCreateDto bulkExportCreateDto;
}
