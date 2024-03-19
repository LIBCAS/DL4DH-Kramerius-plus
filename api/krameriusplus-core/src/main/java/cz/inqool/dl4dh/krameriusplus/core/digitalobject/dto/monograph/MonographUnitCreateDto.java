package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.monograph;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonographUnitCreateDto extends PublicationCreateDto {

    private MonographUnitDetails details;

    @JsonProperty("part.number.str")
    public void setPartNumber(String partNumber) {
        if (partNumber == null) {
            return;
        }
        if (details == null) {
            details = new MonographUnitDetails();
        }
        details.setPartNumber(partNumber);
    }

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
