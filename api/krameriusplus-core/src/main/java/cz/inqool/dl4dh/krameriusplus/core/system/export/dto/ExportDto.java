package cz.inqool.dl4dh.krameriusplus.core.system.export.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportDto extends DatedObjectDto {

    private String publicationId;

    private String publicationTitle;

    private FileRef fileRef;

    private JobEventDto jobEvent;
}
