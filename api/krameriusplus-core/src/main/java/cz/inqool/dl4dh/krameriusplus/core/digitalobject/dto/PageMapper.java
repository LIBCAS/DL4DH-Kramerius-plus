package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.nametag.NameTagMetadataDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.nametag.NamedEntityDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.nametag.NamedEntityType;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.udpipe.LinguisticMetadataDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.udpipe.TokenDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.LinguisticMetadata;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.NamedEntity;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Token;
import org.mapstruct.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface PageMapper {

    TokenDto toDto(Token token);

    LinguisticMetadataDto toDto(LinguisticMetadata metadata);

    NamedEntityDto toDto(NamedEntity namedEntity);

    NameTagMetadataDto toDto(NameTagMetadata metadata);

    default Map<NamedEntityType, List<NamedEntityDto>> toDto(Map<NamedEntityType, List<NamedEntity>> namedEntityMap) {
        Map<NamedEntityType, List<NamedEntityDto>> result = new HashMap<>();

        for (Map.Entry<NamedEntityType, List<NamedEntity>> entry : namedEntityMap.entrySet()) {
            List<NamedEntityDto> mappedValues = entry.getValue().stream().map(this::toDto).collect(Collectors.toList());
            result.put(entry.getKey(), mappedValues);
        }

        return result;
    }
}
