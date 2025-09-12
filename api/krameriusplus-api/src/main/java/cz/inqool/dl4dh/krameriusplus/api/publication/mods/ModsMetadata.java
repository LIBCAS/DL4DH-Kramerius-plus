package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.ProcessedBy;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModsMetadata {

    private List<ModsTitleInfo> titleInfos = new ArrayList<>();

    private List<ModsName> names = new ArrayList<>();

    private List<ModsGenre> genres = new ArrayList<>();

    private List<ModsOriginInfo> originInfos = new ArrayList<>();

    private List<ModsLanguage> languages = new ArrayList<>();

    private List<ModsPhysicalDescription> physicalDescriptions = new ArrayList<>();

    private List<ModsIdentifier> identifiers = new ArrayList<>();

    // Additional field for TEI
    private List<ProcessedBy> processed_by = new ArrayList<>();
}
