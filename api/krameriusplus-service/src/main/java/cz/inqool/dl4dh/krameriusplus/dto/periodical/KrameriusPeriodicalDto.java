package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.KrameriusModelAware;
import cz.inqool.dl4dh.krameriusplus.dto.KrameriusPublicationDto;

/**
 * @author Norbert Bodnar
 */
public class KrameriusPeriodicalDto extends KrameriusPublicationDto implements KrameriusModelAware {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL;
    }
}
