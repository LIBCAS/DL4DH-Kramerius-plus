package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPhysicalDescription;
import cz.inqool.dl4dh.mods.Extent;
import cz.inqool.dl4dh.mods.PhysicalDescriptionDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ModsPhysicalDescriptionMapper extends ModsMapperBase {

    default ModsPhysicalDescription map(PhysicalDescriptionDefinition element) {
        List<Object> extentObjects = element.getFormOrReformattingQualityOrInternetMediaType().stream()
                .filter(object -> object instanceof Extent).collect(Collectors.toList());

        if (extentObjects.size() != 1) {
            throw new IllegalStateException("Expected size=1 actual=" + extentObjects.size());
        }

        return map(((Extent) extentObjects.get(0)));
    }

    @Mapping(target = "extent", source = "extent.value")
    ModsPhysicalDescription map(Extent extent);
}
