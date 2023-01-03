package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.SUPPLEMENT;


@Getter
@Setter
public class SupplementDto extends PublicationDto {

    private String date;

    @Override
    public String getModel() {
        return SUPPLEMENT;
    }
}
