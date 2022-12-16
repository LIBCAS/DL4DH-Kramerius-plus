package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import cz.inqool.dl4dh.mods.ModsDefinition;
import cz.inqool.dl4dh.mods.TitleInfoDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.lte;

@Component
public class ModsMapper {

    private ModsTitleMapper titleMapper;

    public ModsMetadata map(ModsCollectionDefinition element) {
        ModsMetadata modsMetadata = new ModsMetadata();
        lte(element.getMods().size(), 1, () -> new IllegalStateException(
                "Root MODS element is expected to have 1 ModsDefinition, but had: " + element.getMods().size()));
        ModsDefinition modsDefinition = element.getMods().get(0);

        for (Object modsGroup : modsDefinition.getModsGroup()) {
            if (modsGroup instanceof TitleInfoDefinition) {
                modsMetadata.getTitleInfos().add(titleMapper.map((TitleInfoDefinition) modsGroup));}
//            } else if (modsGroup instanceof NameDefinition) {
//                modsMetadata.setName(map((NameDefinition) modsGroup));
//            } else if (modsGroup instanceof GenreDefinition) {
//                modsMetadata.setGenre(map((GenreDefinition) modsGroup));
//            } else if (modsGroup instanceof PhysicalDescriptionDefinition) {
//                modsMetadata.setPhysicalDescription(map((PhysicalDescriptionDefinition) modsGroup));
//            } else if (modsGroup instanceof OriginInfoDefinition) {
//                modsMetadata.setOriginInfo(map((OriginInfoDefinition) modsGroup));
//            } else if (modsGroup instanceof IdentifierDefinition) {
//                modsMetadata.getIdentifiers().add(map((IdentifierDefinition) modsGroup));
//            }
        }

        return modsMetadata;
    }

    @Autowired
    public void setTitleMapper(ModsTitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

//    ModsTitleInfo map(TitleInfoDefinition element);
//
//    default List<String> map(List<Object> objects) {
//        List<String> result = new ArrayList<>();
//        for (Object o : objects) {
//            if (o instanceof String) {
//                result.add((String) o);
//            }
//        }
//
//        return result;
//    }

//    ModsName map(NameDefinition element);
//
//    ModsGenre map(GenreDefinition element);
//
//    ModsPhysicalDescription map(PhysicalDescriptionDefinition element);
//
//    ModsOriginInfo map(OriginInfoDefinition element);
//
//    ModsIdentifier map(IdentifierDefinition element);
}
