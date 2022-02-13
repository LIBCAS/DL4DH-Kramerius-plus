package cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.metadata;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.ModsMetadata;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import cz.inqool.dl4dh.mods.ModsDefinition;
import lombok.Getter;

import java.util.List;

@Getter
public class ModsWrapper {

    private final ModsCollectionDefinition modsCollection;

    private ModsMetadata transformedMods;

    public ModsWrapper(ModsCollectionDefinition modsCollection) {
        this.modsCollection = modsCollection;
    }

    public ModsMetadata getTransformedMods() {
        if (transformedMods == null) {
            transformedMods = transform();
        }

        return transformedMods;
    }

    private ModsMetadata transform() {
        List<ModsDefinition> mods = modsCollection.getMods();

        ModsMetadata modsMetadata = new ModsMetadata();

        for (ModsDefinition modsDefinition : mods) {
            for (Object modsGroup : modsDefinition.getModsGroup()) {
                processModsGroup(modsGroup, modsMetadata);
            }
        }

        return modsMetadata;
    }

    private void processModsGroup(Object modsGroup, ModsMetadata modsMetadata) {
        ModsGroupType modsGroupType = ModsGroupType.from(modsGroup.getClass());

        if (modsGroupType != null) {
            modsGroupType.addAttribute(modsGroup, modsMetadata);
        }
    }
}
