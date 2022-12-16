package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.mods.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.lte;

@Component
public class ModsMapper {

    private ModsTitleMapper titleMapper;

    private ModsNameMapper nameMapper;

    private ModsGenreMapper genreMapper;

    private ModsOriginInfoMapper originInfoMapper;

    private ModsLanguageMapper languageMapper;

    private ModsPhysicalDescriptionMapper physicalDescriptionMapper;

    private ModsIdentifierMapper identifierMapper;

    public ModsMetadata map(ModsCollectionDefinition element) {
        ModsMetadata modsMetadata = new ModsMetadata();
        lte(element.getMods().size(), 1, () -> new IllegalStateException(
                "Root ModsCollectionDefinition element is expected to have 1 ModsDefinition, but had: " + element.getMods().size()));
        ModsDefinition modsDefinition = element.getMods().get(0);

        for (Object modsGroup : modsDefinition.getModsGroup()) {
            if (modsGroup instanceof TitleInfoDefinition) {
                modsMetadata.getTitleInfos().add(titleMapper.map((TitleInfoDefinition) modsGroup));
            } else if (modsGroup instanceof NameDefinition) {
                modsMetadata.setName(nameMapper.map((NameDefinition) modsGroup));
            } else if (modsGroup instanceof GenreDefinition) {
                modsMetadata.getGenres().add(genreMapper.map((GenreDefinition) modsGroup));
            } else if (modsGroup instanceof OriginInfoDefinition) {
                modsMetadata.getOriginInfo().add(originInfoMapper.map((OriginInfoDefinition) modsGroup));
            } else if (modsGroup instanceof LanguageDefinition) {
                modsMetadata.getLanguages().add(languageMapper.map((LanguageDefinition) modsGroup));
            } else if (modsGroup instanceof PhysicalDescriptionDefinition) {
                modsMetadata.setPhysicalDescription(physicalDescriptionMapper.map((PhysicalDescriptionDefinition) modsGroup));
            } else if (modsGroup instanceof IdentifierDefinition) {
                modsMetadata.getIdentifiers().add(identifierMapper.map((IdentifierDefinition) modsGroup));
            }
        }

        return modsMetadata;
    }

    @Autowired
    public void setTitleMapper(ModsTitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

    @Autowired
    public void setNameMapper(ModsNameMapper nameMapper) {
        this.nameMapper = nameMapper;
    }

    @Autowired
    public void setGenreMapper(ModsGenreMapper genreMapper) {
        this.genreMapper = genreMapper;
    }

    @Autowired
    public void setOriginInfoMapper(ModsOriginInfoMapper originInfoMapper) {
        this.originInfoMapper = originInfoMapper;
    }

    @Autowired
    public void setLanguageMapper(ModsLanguageMapper languageMapper) {
        this.languageMapper = languageMapper;
    }

    @Autowired
    public void setPhysicalDescriptionMapper(ModsPhysicalDescriptionMapper physicalDescriptionMapper) {
        this.physicalDescriptionMapper = physicalDescriptionMapper;
    }

    @Autowired
    public void setIdentifierMapper(ModsIdentifierMapper identifierMapper) {
        this.identifierMapper = identifierMapper;
    }
}
