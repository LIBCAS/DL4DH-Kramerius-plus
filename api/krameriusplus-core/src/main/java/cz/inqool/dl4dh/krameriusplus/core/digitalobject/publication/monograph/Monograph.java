package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.MonographDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.MONOGRAPH;


/**
 * Object representing a Monograph. Monographs contain either monograph units as children, or directly pages. Can
 * not contain both
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(MONOGRAPH)
public class Monograph extends Publication {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.MONOGRAPH;
    }

    @Override
    public MonographDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}
