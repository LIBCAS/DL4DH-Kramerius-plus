package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.other;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementCreateDto extends PublicationCreateDto {

    private SupplementDetails details = new SupplementDetails();

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
