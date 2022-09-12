package cz.inqool.dl4dh.krameriusplus.core.system.export.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkExportCreateDto extends DatedObjectCreateDto {

    // TODO: zmenit na JobPlanCreateDto
    private JobEvent jobEvent;
}
