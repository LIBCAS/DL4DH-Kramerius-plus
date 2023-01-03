package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.MONOGRAPH;

@Getter
@Setter
public class MonographDto extends PublicationDto {
    @Override
    public String getModel() {
        return MONOGRAPH;
    }
}
