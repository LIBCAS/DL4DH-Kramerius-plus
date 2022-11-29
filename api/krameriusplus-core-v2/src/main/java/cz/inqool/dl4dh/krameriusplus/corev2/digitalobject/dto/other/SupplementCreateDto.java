package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.other;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementCreateDto extends PublicationCreateDto {

    private SupplementDetails details;
}
