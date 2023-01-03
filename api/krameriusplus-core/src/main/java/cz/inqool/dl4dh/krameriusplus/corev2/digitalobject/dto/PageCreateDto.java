package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
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
