package cz.inqool.dl4dh.krameriusplus.domain.dto;

import cz.inqool.dl4dh.krameriusplus.domain.enums.PublicationModel;

/**
 * @author Norbert Bodnar
 */
public class KrameriusPeriodicalDto extends KrameriusPublicationDto {

    @Override
    public PublicationModel getModel() {
        return PublicationModel.PERIODICAL;
    }
}
