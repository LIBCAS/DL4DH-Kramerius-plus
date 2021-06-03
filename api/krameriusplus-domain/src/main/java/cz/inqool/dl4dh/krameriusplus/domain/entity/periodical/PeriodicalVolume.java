package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PERIODICAL_VOLUME;

/**
 * Represents a volume for a periodical. One periodical can have multiple volumes. Volumes are mostly identified
 * by a year.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalVolume extends Publication {

    private String volumeNumber;

    /**
     * Should be the same as volumeNumber, might delete later. As for now, some publications have different numbers
     * in these two attributes, so keeping both
     */
    private String volumeYear;

    private String parentId;

    @Transient
    private List<PeriodicalItem> periodicalItems = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return PERIODICAL_VOLUME;
    }

    @Override
    public List<Page> getPages() {
        if (periodicalItems == null || periodicalItems.isEmpty()) {
            throw new IllegalStateException("PeriodicalVolume contains no items");
        }

        return periodicalItems
                .stream()
                .map(PeriodicalItem::getPages)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
