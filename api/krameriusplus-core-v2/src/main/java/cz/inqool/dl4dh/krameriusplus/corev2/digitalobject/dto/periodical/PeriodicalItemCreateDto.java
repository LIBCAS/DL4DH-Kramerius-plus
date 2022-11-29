package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.periodical;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.PublicationCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodicalItemCreateDto extends PublicationCreateDto {

    private PeriodicalItemDetails details;
}
