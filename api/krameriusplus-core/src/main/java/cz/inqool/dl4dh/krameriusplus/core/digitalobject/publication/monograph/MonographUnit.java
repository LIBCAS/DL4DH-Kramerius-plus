package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph;

import com.fasterxml.jackson.annotation.JsonAlias;
import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.MONOGRAPH_UNIT;


/**
 * Object representing a MonographUnit. MonographUnits must contain pages directly as children
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(MONOGRAPH_UNIT)
public class MonographUnit extends Publication {

    @JsonAlias({"part.number.str"})
    private String partNumber;

    private String partTitle;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.MONOGRAPH_UNIT;
    }

    @Override
    public MonographUnitDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}
