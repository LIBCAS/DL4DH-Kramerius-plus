package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsGenre;
import cz.inqool.dl4dh.mods.GenreDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ModsGenreMapper extends ModsMapperBase {

    ModsGenre map(GenreDefinition modsGroup);
}
