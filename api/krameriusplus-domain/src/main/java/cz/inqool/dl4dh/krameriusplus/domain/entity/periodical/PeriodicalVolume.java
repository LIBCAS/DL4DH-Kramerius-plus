package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PERIODICAL_VOLUME;

/**
 * Represents a volume for a periodical. One periodical can have multiple volumes. Volumes are mostly identified
 * by a year.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public class PeriodicalVolume extends Publication {

    private String volumeNumber;

    /**
     * Should be the same as volumeNumber, might delete later. As for now, some publications have different numbers
     * in these two attributes, so keeping both
     */
    private String volumeYear;

    @Transient
    private List<PeriodicalItem> periodicalItems;

    @Override
    public KrameriusModel getModel() {
        return PERIODICAL_VOLUME;
    }
}
