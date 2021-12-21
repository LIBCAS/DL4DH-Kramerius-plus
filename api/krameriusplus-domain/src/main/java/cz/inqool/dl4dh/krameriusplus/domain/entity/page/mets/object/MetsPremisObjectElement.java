package cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.object;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.MetsElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetsPremisObjectElement extends MetsElement {

    private ObjectIdentifier objectIdentifier;

    private ObjectCharacteristics objectCharacteristics;
}
