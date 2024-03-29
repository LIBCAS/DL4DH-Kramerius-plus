package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodicalItemCreateDto extends PublicationCreateDto {

    private PeriodicalItemDetails details;

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
