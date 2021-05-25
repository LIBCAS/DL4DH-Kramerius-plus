package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalVolumeDto extends PublicationDto<PeriodicalVolume> {

    private String volumeNumber;

    private String year;

    private String rootTitle;

    private String rootPid;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        volumeNumber = (String) details.get("volumeNumber");
        year = ((String) details.get("year"));
    }

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL_VOLUME;
    }

    @Override
    public PeriodicalVolume toEntity() {
        return null;
    }
}
