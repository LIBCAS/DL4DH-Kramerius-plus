package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.ExportJobConfigMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.bulk.BulkExportMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ExportJobConfigMapper.class,
        BulkExportMapper.class,
        KrameriusJobInstanceMapper.class})
public interface ExportRequestMapper extends DatedObjectMapper<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {
}
