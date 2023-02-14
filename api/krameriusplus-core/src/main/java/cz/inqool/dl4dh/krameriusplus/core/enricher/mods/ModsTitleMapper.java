package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsTitleInfo;
import cz.inqool.dl4dh.mods.TitleInfoDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ModsTitleMapper extends ModsMapperBase {

    @Mappings({
            @Mapping(target = "titles", expression = "java(extractStringsFromListOfObjects(element.getTitleOrSubTitleOrPartNumber(), \"title\"))"),
            @Mapping(target = "subTitles", expression = "java(extractStringsFromListOfObjects(element.getTitleOrSubTitleOrPartNumber(), \"subTitle\"))"),
            @Mapping(target = "type", source = "typeAtt")
    })
    ModsTitleInfo map(TitleInfoDefinition element);
}
