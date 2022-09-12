package cz.inqool.dl4dh.krameriusplus.core.system.export.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BulkExportDto extends DatedObjectDto {

    private FileRef fileRef;

    // TODO: zmenit na JobPlanDto
    @NotNull
    private JobEventDto jobEventDto;

    private Set<ExportDto> exports = new HashSet<>();
}
