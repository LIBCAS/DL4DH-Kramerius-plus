package cz.inqool.dl4dh.krameriusplus.core.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceMapper;
import cz.inqool.dl4dh.krameriusplus.core.job.config.export.ExportJobConfigMapper;
import cz.inqool.dl4dh.krameriusplus.core.request.RequestMapper;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItemMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
                ExportJobConfigMapper.class,
                ExportRequestItemMapper.class,
                KrameriusJobInstanceMapper.class,
        })
public abstract class ExportRequestMapper extends RequestMapper<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {
}
