package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.dao.cascade.CascadeSave;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
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
public class PeriodicalVolume extends Publication implements ParentAware {

    private String volumeNumber;

    /**
     * Should be the same as volumeNumber, might delete later. As for now, some publications have different numbers
     * in these two attributes, so keeping both
     */
    private String volumeYear;

    private String parentId;

    private Integer index;

    @DBRef
    @CascadeSave
    private List<PeriodicalItem> periodicalItems = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return PERIODICAL_VOLUME;
    }

    @Override
    public List<? extends Publication> getChildren() {
        return periodicalItems;
    }
}
