package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsLanguage;
import cz.inqool.dl4dh.mods.LanguageDefinition;
import cz.inqool.dl4dh.mods.LanguageTermDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ModsLanguageMapper extends ModsMapperBase {

    default ModsLanguage map(LanguageDefinition element) {
        if (element.getLanguageTerm().isEmpty()) {
            return null;
        }

        if (element.getLanguageTerm().size() > 1) {
            throw new IllegalStateException("Expected at most 1 element <languageTerm>, but found:" + element.getLanguageTerm().size());
        }

        ModsLanguage modsLanguage = map(element.getLanguageTerm().get(0));
        modsLanguage.setObjectPart(element.getObjectPart());

        return modsLanguage;
    }

    ModsLanguage map(LanguageTermDefinition element);
}
