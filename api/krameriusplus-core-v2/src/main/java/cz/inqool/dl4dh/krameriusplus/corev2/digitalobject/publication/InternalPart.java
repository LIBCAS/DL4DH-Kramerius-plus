package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalPart extends Publication {

    private String pageRange;

    private String partTitle;

    private String partType;

    private String pageNumber;
}
