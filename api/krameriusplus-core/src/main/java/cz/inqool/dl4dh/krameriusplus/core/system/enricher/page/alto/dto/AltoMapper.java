package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.alto.PageSpaceType;
import cz.inqool.dl4dh.alto.StringType;
import cz.inqool.dl4dh.alto.TextBlockType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface AltoMapper {

    AltoDto toAltoDto(Alto alto);

    default PageSpaceTypeDto toPageSpaceTypeDto(PageSpaceType blockType) {
        PageSpaceTypeDto dto = new PageSpaceTypeDto();
        dto.setBlockTypes(blockType.getTextBlockOrIllustrationOrGraphicalElement()
                .stream()
                .filter(block -> block instanceof TextBlockType)
                .map(block -> toTextBlockTypeDto((TextBlockType) block))
                .collect(Collectors.toList()));

        return dto;
    }

    TextBlockTypeDto toTextBlockTypeDto(TextBlockType textBlockType);


    default TextBlockTypeDto.TextLineDto toTextLine(TextBlockType.TextLine textLine) {
        TextBlockTypeDto.TextLineDto dto = new TextBlockTypeDto.TextLineDto();
        dto.setStringAndSP(textLine.getStringAndSP()
                .stream()
                .map(obj -> {
                    if (obj instanceof StringType) {
                        return toStringTypeDto((StringType) obj);
                    } else if (obj instanceof TextBlockType.TextLine.SP) {
                        return toSpDto((TextBlockType.TextLine.SP) obj);
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        return dto;
    }

    @Mapping(source = "ID", target = "id")
    @Mapping(source = "HEIGHT", target = "height")
    @Mapping(source = "WIDTH", target = "width")
    @Mapping(source = "HPOS", target = "hpos")
    @Mapping(source = "VPOS", target = "vpos")
    @Mapping(source = "CONTENT", target = "content")
    @Mapping(source = "SUBSCONTENT", target = "subscontent")
    @Mapping(source = "SUBSTYPE", target = "substype")
    TextBlockTypeDto.TextLineDto.StringTypeDto toStringTypeDto(StringType stringType);

    @Mapping(source = "ID", target = "id")
    @Mapping(source = "HEIGHT", target = "height")
    @Mapping(source = "WIDTH", target = "width")
    @Mapping(source = "HPOS", target = "hpos")
    @Mapping(source = "VPOS", target = "vpos")
    TextBlockTypeDto.TextLineDto.SpDto toSpDto(TextBlockType.TextLine.SP sp);
}
