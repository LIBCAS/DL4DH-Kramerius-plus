package cz.inqool.dl4dh.krameriusplus.domain.dto;

import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;

/**
 * @author Norbert Bodnar
 */
public class KrameriusPeriodicalDto extends KrameriusPublicationDto implements KrameriusModelAware {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL;
    }
}
