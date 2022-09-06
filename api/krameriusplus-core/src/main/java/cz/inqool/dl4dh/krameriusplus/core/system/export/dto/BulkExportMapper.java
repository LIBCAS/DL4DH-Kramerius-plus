package cz.inqool.dl4dh.krameriusplus.core.system.export.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExport;
import org.mapstruct.Mapper;

@Mapper
public interface BulkExportMapper extends DatedObjectMapper<BulkExport, BulkExportCreateDto, BulkExportDto> {
}
