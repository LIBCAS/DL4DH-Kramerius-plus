package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsIdentifier;
import cz.inqool.dl4dh.mods.IdentifierDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ModsIdentifierMapper extends ModsMapperBase {

    ModsIdentifier map(IdentifierDefinition element);
}
