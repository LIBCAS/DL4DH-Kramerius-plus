package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PERIODICAL_ITEM;

@Getter
@Setter
public class PeriodicalItemDto extends PublicationDto {

    private String date;

    private String issueNumber;

    private String partNumber;

    @Override
    public String getModel() {
        return PERIODICAL_ITEM;
    }
}
