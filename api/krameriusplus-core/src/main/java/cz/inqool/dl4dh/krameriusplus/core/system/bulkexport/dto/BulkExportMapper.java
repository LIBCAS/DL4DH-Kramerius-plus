package cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExport;
import org.mapstruct.Mapper;

@Mapper
public interface BulkExportMapper extends DatedObjectMapper<BulkExport, BulkExportCreateDto, BulkExportDto> {
}
