package cz.inqool.dl4dh.krameriusplus.core.system.export.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportDto extends DatedObjectDto {

    private String publicationId;

    private String publicationTitle;

    private FileRef fileRef;
}
