package cz.inqool.dl4dh.krameriusplus.core.request.export.export;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRefMapper;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        FileRefMapper.class,
        KrameriusJobInstanceMapper.class
})
public interface ExportMapper {

    @Mapping(target = "children", source = "childrenList")
    ExportDto toDto(Export export);

}
