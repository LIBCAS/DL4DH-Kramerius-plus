package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PERIODICAL_VOLUME;

@Getter
@Setter
public class PeriodicalVolumeDto extends PublicationDto {

    private String volumeNumber;

    private String volumeYear;

    @Override
    public String getModel() {
        return PERIODICAL_VOLUME;
    }
}
