package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public abstract class MonographDto extends PublicationDto<Monograph> {

    private String donator;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.MONOGRAPH;
    }
}
