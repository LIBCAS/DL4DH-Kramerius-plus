package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageCreateDto extends DigitalObjectCreateDto {

    private PageDetails details;

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
