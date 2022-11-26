package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PERIODICAL_VOLUME;

@Getter
@Setter
@JsonTypeName(PERIODICAL_VOLUME)
public class PeriodicalVolumeDto extends PublicationDto {

    private String volumeNumber;

    private String volumeYear;
}
