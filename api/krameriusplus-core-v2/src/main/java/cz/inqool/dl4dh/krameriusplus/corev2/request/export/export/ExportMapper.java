package cz.inqool.dl4dh.krameriusplus.corev2.request.export.export;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportDto;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRefMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Mapper(uses = {
        FileRefMapper.class,
        KrameriusJobInstanceMapper.class
})
public interface ExportMapper {

    ExportDto toDto(Export export);

    default List<ExportDto> toListDto(Map<Long, Export> entityMap) {
        return new TreeMap<>(entityMap).values()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
