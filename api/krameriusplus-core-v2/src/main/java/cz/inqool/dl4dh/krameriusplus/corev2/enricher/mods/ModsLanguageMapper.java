package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsLanguage;
import cz.inqool.dl4dh.mods.LanguageDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ModsLanguageMapper extends ModsMapperBase {

    ModsLanguage map(LanguageDefinition element);
}
