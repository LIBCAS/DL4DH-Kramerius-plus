package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodicalVolumeCreateDto extends PublicationCreateDto {

    private PeriodicalVolumeDetails details;

    @JsonProperty("date.str")
    public void setYear(String year) {
        if (year == null) {
            return;
        }
        if (details == null) {
            details = new PeriodicalVolumeDetails();
        }

        details.setYear(year);
    }

    @JsonProperty("part.number.str")
    public void setVolumeNumber(String volumeNumber) {
        if (volumeNumber == null) {
            return;
        }
        if (details == null) {
            details = new PeriodicalVolumeDetails();
        }

        details.setVolumeNumber(volumeNumber);
    }

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
