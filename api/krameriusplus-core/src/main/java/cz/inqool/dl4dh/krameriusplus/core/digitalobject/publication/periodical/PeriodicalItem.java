package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PeriodicalItemDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.PERIODICAL_ITEM;


/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(PERIODICAL_ITEM)
public class PeriodicalItem extends Publication {

    private String date;

    private String issueNumber;

    private String partNumber;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL_ITEM;
    }

    @Override
    public PeriodicalItemDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}
