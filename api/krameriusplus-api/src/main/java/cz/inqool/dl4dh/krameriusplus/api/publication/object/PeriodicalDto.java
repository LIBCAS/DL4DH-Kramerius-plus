package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PublicationModelName.PERIODICAL;


@Getter
@Setter
public class PeriodicalDto extends PublicationDto {
    @Override
    public String getModel() {
        return PERIODICAL;
    }
}
