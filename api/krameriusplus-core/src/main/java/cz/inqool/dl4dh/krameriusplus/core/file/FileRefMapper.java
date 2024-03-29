package cz.inqool.dl4dh.krameriusplus.core.file;

import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import org.mapstruct.Mapper;

@Mapper
public interface FileRefMapper {

    FileRefDto toDto(FileRef entity);

    FileRef fromDto(FileRefDto dto);
}
