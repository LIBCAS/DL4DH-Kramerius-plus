package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModsLanguage {

    private List<ModsLanguageTerm> languageTerms = new ArrayList<>();

    private String objectPart;
}
