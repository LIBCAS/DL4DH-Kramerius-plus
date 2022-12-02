package cz.inqool.dl4dh.krameriusplus.api.publication.mods;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModsIdentifier {

    private String type;

    private Boolean invalid;

    private String value;
}
