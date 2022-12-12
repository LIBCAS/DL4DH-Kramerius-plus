package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.export.ExportJobConfigMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.RequestMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItemMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ExportJobConfigMapper.class,
        ExportRequestItemMapper.class,
        KrameriusJobInstanceMapper.class,
}, componentModel = "spring")
public abstract class ExportRequestMapper extends RequestMapper<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {
}
