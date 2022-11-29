package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonographUnitCreateDto extends PublicationCreateDto {

    private MonographUnitDetails details;
}
