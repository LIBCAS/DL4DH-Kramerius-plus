package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPhysicalDescription;
import cz.inqool.dl4dh.mods.Extent;
import cz.inqool.dl4dh.mods.PhysicalDescriptionDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ModsPhysicalDescriptionMapper extends ModsMapperBase {

    default ModsPhysicalDescription map(PhysicalDescriptionDefinition element) {
        if (element.getFormOrReformattingQualityOrInternetMediaType().size() != 1) {
            throw new IllegalStateException("Expected size=1 actual=" + element.getFormOrReformattingQualityOrInternetMediaType().size());
        }

        Object object = element.getFormOrReformattingQualityOrInternetMediaType().get(0);

        if (!(object instanceof Extent)) {
            throw new IllegalStateException("Expected: Extent.class, Actual: " + object.getClass().getSimpleName());
        }

        return map(((Extent) object));
    }

    @Mapping(target = "extent", source = "extent.value")
     ModsPhysicalDescription map(Extent extent);
}
