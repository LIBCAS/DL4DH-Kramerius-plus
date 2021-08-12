package cz.inqool.dl4dh.krameriusplus.metadata;

import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import cz.inqool.dl4dh.mods.ModsDefinition;
import lombok.Getter;

import java.util.List;

@Getter
public class ModsAdapter {

    private final ModsCollectionDefinition modsCollection;

    private ModsMetadata transformedMods;

    public ModsAdapter(ModsCollectionDefinition modsCollection) {
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
