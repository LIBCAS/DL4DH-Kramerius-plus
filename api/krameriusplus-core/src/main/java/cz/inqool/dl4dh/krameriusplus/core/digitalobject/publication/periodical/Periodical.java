package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.PERIODICAL;


/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(PERIODICAL)
public class Periodical extends Publication {
    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL;
    }

    @Override
    public PeriodicalDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}
