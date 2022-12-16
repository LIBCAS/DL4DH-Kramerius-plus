package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsName;
import cz.inqool.dl4dh.mods.NameDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ModsNameMapper extends ModsMapperBase {

    @Mappings({
            @Mapping(target = "type", source = "typeAtt"),
            @Mapping(target = "namePart", expression = "java(mapJaxbElements(element.getNamePartOrDisplayFormOrAffiliation(), \"namePart\"))"),
            @Mapping(target = "nameIdentifier", expression = "java(mapJaxbElements(element.getNamePartOrDisplayFormOrAffiliation(), \"nameIdentifier\"))")
    })
    ModsName map(NameDefinition element);
}
