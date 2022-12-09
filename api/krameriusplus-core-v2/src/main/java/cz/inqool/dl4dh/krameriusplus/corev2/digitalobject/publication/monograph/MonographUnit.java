package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.monograph;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.MONOGRAPH_UNIT;


/**
 * Object representing a MonographUnit. MonographUnits must contain pages directly as children
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(MONOGRAPH_UNIT)
public class MonographUnit extends Publication {

    private String partNumber;

    private String partTitle;

    @Override
    public String getModel() {
        return MONOGRAPH_UNIT;
    }
}
