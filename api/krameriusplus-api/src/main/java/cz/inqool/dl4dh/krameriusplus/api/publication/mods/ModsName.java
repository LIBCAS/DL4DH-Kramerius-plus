package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModsName {

    private String type;

    private List<ModsNamePart> nameParts = new ArrayList<>();

    private List<String> nameIdentifier = new ArrayList<>();
}
