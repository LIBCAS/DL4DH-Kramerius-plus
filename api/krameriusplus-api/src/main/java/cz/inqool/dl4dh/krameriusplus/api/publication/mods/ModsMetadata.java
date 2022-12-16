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

    private ModsGenre genre;

    private ModsPhysicalDescription physicalDescription;

    private ModsOriginInfo originInfo;

    private List<ModsIdentifier> identifiers = new ArrayList<>();
}
