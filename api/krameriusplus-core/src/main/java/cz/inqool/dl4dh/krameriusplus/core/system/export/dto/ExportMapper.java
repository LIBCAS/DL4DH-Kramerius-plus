package cz.inqool.dl4dh.krameriusplus.core.system.export.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import org.mapstruct.Mapper;

@Mapper
public interface ExportMapper extends DatedObjectMapper<Export, ExportCreateDto, ExportDto> {
}
