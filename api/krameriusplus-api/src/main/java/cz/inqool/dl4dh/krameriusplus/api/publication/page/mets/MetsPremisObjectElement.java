package cz.inqool.dl4dh.krameriusplus.api.publication.page.mets;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetsPremisObjectElement extends MetsElement {

    private ObjectIdentifier objectIdentifier;

    private ObjectCharacteristics objectCharacteristics;
}
