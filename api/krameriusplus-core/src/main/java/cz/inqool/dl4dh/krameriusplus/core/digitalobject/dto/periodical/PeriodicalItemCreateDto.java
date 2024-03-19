package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical;

import com.fasterxml.jackson.annotation.JsonSetter;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodicalItemCreateDto extends PublicationCreateDto {

    private PeriodicalItemDetails details;

    // v7 API compatibility fix
    @JsonSetter("date.str")
    private void setDate(String date) {
        if (date == null) {
            return;
        }
        if (details == null) {
            details = new PeriodicalItemDetails();
        }
        details.setDate(date);
    }

    @JsonSetter("part.number.str")
    private void setPartNumber(String partNumber) {
        if (partNumber == null) {
            return;
        }
        if (details == null) {
            details = new PeriodicalItemDetails();
        }
        details.setPartNumber(partNumber);
    }

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
