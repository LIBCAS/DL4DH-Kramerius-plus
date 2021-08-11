package cz.inqool.dl4dh.krameriusplus.service.utils;

import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import cz.inqool.dl4dh.mods.ModsDefinition;

import java.util.List;
import java.util.Optional;

/**
 * Static Utils class for working with Mods object
 */
public class ModsUtils {

    public static void enrichPublicationWithModsMetadata(Publication publication, ModsDefinition mods) {

    }

    public static ModsMetadata getModsMetadata(ModsCollectionDefinition modsCollection) {
        List<ModsDefinition> mods = modsCollection.getMods();

        ModsMetadata modsMetadata = new ModsMetadata();

        for (ModsDefinition modsDefinition : mods) {
            for (Object modsGroup : modsDefinition.getModsGroup()) {
                processModsGroup(modsGroup, modsMetadata);
            }
        }

        return modsMetadata;
    }

    private static void processModsGroup(Object modsGroup, ModsMetadata modsMetadata) {
        ModsGroupType modsGroupType = ModsGroupType.from(modsGroup.getClass());

        if (modsGroupType != null) {
            modsGroupType.addAttribute(modsGroup, modsMetadata);
        }
    }

    public static Optional<Object> getAttribute(ModsDefinition modsDefinition, Class<? extends Object> attribute) {
        for (Object o : modsDefinition.getModsGroup()) {
            if (attribute.isInstance(o)) {
                return Optional.of(attribute.cast(o));
            }
        }

        return Optional.empty();
    }
}
