package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsName;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsNamePart;
import cz.inqool.dl4dh.mods.NameDefinition;
import cz.inqool.dl4dh.mods.NamePartDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ModsNameMapper extends ModsMapperBase {

    @Mappings({
            @Mapping(target = "type", source = "typeAtt"),
            @Mapping(target = "nameParts", expression = "java(mapModsNamePart(element.getNamePartOrDisplayFormOrAffiliation()))"),
            @Mapping(target = "nameIdentifier", expression = "java(mapJaxbElements(element.getNamePartOrDisplayFormOrAffiliation(), \"nameIdentifier\"))")
    })
    ModsName map(NameDefinition element);

    default List<ModsNamePart> mapModsNamePart(List<JAXBElement<?>> elements) {
        if (elements == null) {
            return new ArrayList<>();
        }

        List<ModsNamePart> result = new ArrayList<>();

        for (JAXBElement<?> element : elements) {
            if (element.getValue() instanceof NamePartDefinition) {
                NamePartDefinition namePartDefinition = (NamePartDefinition) element.getValue();

                ModsNamePart namePart = new ModsNamePart();

                namePart.setType(namePartDefinition.getType());
                namePart.setValue(namePartDefinition.getValue());

                result.add(namePart);
            }
        }

        return result;
    }
}
