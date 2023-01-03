package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PublicationModelName.MONOGRAPH_UNIT;


@Getter
@Setter
public class MonographUnitDto extends PublicationDto {

    private String partNumber;

    private String partTitle;

    @Override
    public String getModel() {
        return MONOGRAPH_UNIT;
    }
}
