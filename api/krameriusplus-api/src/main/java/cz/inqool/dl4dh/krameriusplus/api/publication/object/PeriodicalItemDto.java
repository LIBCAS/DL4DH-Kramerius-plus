package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PERIODICAL_ITEM;

@Getter
@Setter
@JsonTypeName(PERIODICAL_ITEM)
public class PeriodicalItemDto extends PublicationDto {

    private String date;

    private String issueNumber;

    private String partNumber;
}
