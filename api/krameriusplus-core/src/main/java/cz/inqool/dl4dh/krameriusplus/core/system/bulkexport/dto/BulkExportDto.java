package cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BulkExportDto extends DatedObjectDto {

    private FileRef fileRef;

    private Set<ExportDto> exports = new HashSet<>();

    private ExportFormat format;
}
