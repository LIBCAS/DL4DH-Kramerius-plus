package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.MONOGRAPH;

/**
 * Object representing a Monograph. Monographs contain either monograph units as children, or directly pages. Can
 * not contain both
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
public abstract class Monograph extends Publication {

    private String donator;

    @Override
    public KrameriusModel getModel() {
        return MONOGRAPH;
    }
}
