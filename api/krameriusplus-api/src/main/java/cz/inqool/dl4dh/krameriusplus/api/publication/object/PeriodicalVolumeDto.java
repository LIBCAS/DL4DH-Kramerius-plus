package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PeriodicalVolumeDto extends PublicationDto {

    private String volumeNumber;

    private String volumeYear;

}
