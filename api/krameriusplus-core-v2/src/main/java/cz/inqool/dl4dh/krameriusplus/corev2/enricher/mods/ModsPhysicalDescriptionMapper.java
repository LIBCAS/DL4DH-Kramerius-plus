package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsPhysicalDescription;
import cz.inqool.dl4dh.mods.PhysicalDescriptionDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ModsPhysicalDescriptionMapper extends ModsMapperBase {

    ModsPhysicalDescription map(PhysicalDescriptionDefinition element);
}
