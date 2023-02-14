package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsLanguage;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsLanguageTerm;
import cz.inqool.dl4dh.mods.LanguageDefinition;
import cz.inqool.dl4dh.mods.LanguageTermDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ModsLanguageMapper extends ModsMapperBase {

    default ModsLanguage map(LanguageDefinition element) {
        if (element.getLanguageTerm() == null || element.getLanguageTerm().isEmpty()) {
            return null;
        }

        ModsLanguage modsLanguage = new ModsLanguage();

        modsLanguage.setObjectPart(element.getObjectPart());

        if (element.getLanguageTerm() != null) {
            for (LanguageTermDefinition termDefinition : element.getLanguageTerm()) {
                ModsLanguageTerm languageTerm = new ModsLanguageTerm();
                languageTerm.setAuthority(termDefinition.getAuthority());
                languageTerm.setType(termDefinition.getType().value());
                languageTerm.setValue(termDefinition.getValue());

                modsLanguage.getLanguageTerms().add(languageTerm);
            }
        }

        return modsLanguage;
    }
}
