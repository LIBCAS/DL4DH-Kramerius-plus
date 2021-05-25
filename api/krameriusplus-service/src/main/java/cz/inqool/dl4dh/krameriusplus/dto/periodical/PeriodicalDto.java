package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.KrameriusModelAware;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;

/**
 * @author Norbert Bodnar
 */
public class PeriodicalDto extends PublicationDto implements KrameriusModelAware {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL;
    }
}
