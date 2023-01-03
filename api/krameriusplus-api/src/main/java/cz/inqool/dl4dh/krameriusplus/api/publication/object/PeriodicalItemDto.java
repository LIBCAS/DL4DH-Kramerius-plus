package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PeriodicalItemDto extends PublicationDto {

    private String date;

    private String issueNumber;

    private String partNumber;
}
