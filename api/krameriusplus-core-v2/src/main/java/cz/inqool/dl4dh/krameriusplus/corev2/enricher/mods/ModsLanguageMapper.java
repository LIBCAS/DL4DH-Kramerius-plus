package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsLanguage;
import cz.inqool.dl4dh.mods.LanguageDefinition;
import cz.inqool.dl4dh.mods.LanguageTermDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ModsLanguageMapper extends ModsMapperBase {

    default ModsLanguage map(LanguageDefinition element) {
        if (element.getLanguageTerm().size() != 1) {
            throw new IllegalStateException("Expected size=1 actual=" + element.getLanguageTerm().size());
        }

        return map(element.getLanguageTerm().get(0));
    }

    ModsLanguage map(LanguageTermDefinition element);
}
