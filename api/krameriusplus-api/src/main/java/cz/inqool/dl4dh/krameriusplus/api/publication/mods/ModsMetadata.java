package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ModsMetadata {

    private List<ModsTitleInfo> titleInfos = new ArrayList<>();

    private ModsName name;

    private List<ModsGenre> genres = new ArrayList<>();

    private List<ModsOriginInfo> originInfo = new ArrayList<>();

    private List<ModsLanguage> languages = new ArrayList<>();

    private ModsPhysicalDescription physicalDescription;

    private List<ModsIdentifier> identifiers = new ArrayList<>();
}