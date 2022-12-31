package cz.inqool.dl4dh.krameriusplus.corev2.request.export.export;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportDto;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRefMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Mapper(uses = {
        FileRefMapper.class,
        KrameriusJobInstanceMapper.class
})
public interface ExportMapper {

    @Mapping(target = "children", source = "childrenList")
    ExportDto toDto(Export export);

}
