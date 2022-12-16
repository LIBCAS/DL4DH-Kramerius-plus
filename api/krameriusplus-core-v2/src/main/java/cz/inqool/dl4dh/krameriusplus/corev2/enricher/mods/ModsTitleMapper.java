package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsTitleInfo;
import cz.inqool.dl4dh.mods.TitleInfoDefinition;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ModsTitleMapper {

    ModsTitleInfo map(TitleInfoDefinition element);

    default List<String> map(String value) {
        return List.of(value);
    }
}
