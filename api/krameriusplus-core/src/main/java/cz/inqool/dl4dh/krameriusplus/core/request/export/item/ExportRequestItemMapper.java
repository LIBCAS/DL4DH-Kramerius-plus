package cz.inqool.dl4dh.krameriusplus.core.request.export.item;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestItemDto;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.ExportMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ExportMapper.class
})
public interface ExportRequestItemMapper {

    ExportRequestItemDto toDto(ExportRequestItem entity);
}
