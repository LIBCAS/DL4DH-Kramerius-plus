package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.INTERNAL_PART;

@Getter
@Setter
@TypeAlias(INTERNAL_PART)
public class InternalPart extends Publication {

    private String pageRange;

    private String partTitle;

    private String partType;

    private String pageNumber;

    @Override
    public String getModel() {
        return INTERNAL_PART;
    }
}
