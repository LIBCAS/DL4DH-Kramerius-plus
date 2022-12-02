package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.mods.*;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.lte;

@Component
public class ModsMapper {

    public ModsMetadata map(ModsCollectionDefinition element) {
        ModsMetadata modsMetadata = new ModsMetadata();
        lte(element.getMods().size(), 1, () -> new IllegalStateException(
                "Root MODS element is expected to have 1 ModsDefinition, but had: " + element.getMods().size()));
        ModsDefinition modsDefinition = element.getMods().get(0);

        for (Object modsGroup : modsDefinition.getModsGroup()) {
            if (modsGroup instanceof TitleInfoDefinition) {
                handle((TitleInfoDefinition) modsGroup);
            } else if (modsGroup instanceof NameDefinition) {
                handle((NameDefinition) modsGroup);
            } else if (modsGroup instanceof PhysicalDescriptionDefinition) {
                handle((PhysicalDescriptionDefinition) modsGroup);
            } else if (modsGroup instanceof OriginInfoDefinition) {
                handle((OriginInfoDefinition) modsGroup);
            } else if (modsGroup instanceof IdentifierDefinition) {
                handle((IdentifierDefinition) modsGroup);
            }
        }

        return modsMetadata;
    }

    private void handle(TitleInfoDefinition element) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    public void handle(NameDefinition element) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    public void handle(PhysicalDescriptionDefinition element) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    public void handle(OriginInfoDefinition element) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    public void handle(IdentifierDefinition element) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}
