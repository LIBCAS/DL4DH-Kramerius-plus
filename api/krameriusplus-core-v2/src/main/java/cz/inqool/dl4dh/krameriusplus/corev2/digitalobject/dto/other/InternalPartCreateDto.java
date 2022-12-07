package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.other;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.PageCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalPartCreateDto extends PageCreateDto {

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
