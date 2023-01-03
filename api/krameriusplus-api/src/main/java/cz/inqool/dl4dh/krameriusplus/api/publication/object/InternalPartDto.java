package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PublicationModelName.INTERNAL_PART;


@Getter
@Setter
public class InternalPartDto extends PublicationDto {

    private String pageRange;

    private String partTitle;

    private String partType;

    private String pageNumber;

    @Override
    public String getModel() {
        return INTERNAL_PART;
    }
}
