package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsTitleInfo;
import cz.inqool.dl4dh.mods.TitleInfoDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ModsTitleMapper extends ModsMapperBase {

    @Mappings({
            @Mapping(target = "title", expression = "java(mapJaxbElementsObjects(element.getTitleOrSubTitleOrPartNumber(), \"title\"))"),
            @Mapping(target = "subTitle", expression = "java(mapJaxbElementsObjects(element.getTitleOrSubTitleOrPartNumber(), \"subTitle\"))"),
            @Mapping(target = "type", source = "typeAtt")
    })
    ModsTitleInfo map(TitleInfoDefinition element);
}
