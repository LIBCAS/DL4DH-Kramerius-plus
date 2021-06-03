package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.MONOGRAPH;

/**
 * Object representing a Monograph. Monographs contain either monograph units as children, or directly pages. Can
 * not contain both
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public abstract class Monograph extends Publication {

    private String donator;

    @Override
    public KrameriusModel getModel() {
        return MONOGRAPH;
    }
}
