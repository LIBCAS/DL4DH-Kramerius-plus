package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.periodical;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PeriodicalVolumeDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.PERIODICAL_VOLUME;


/**
 * Represents a volume for a periodical. One periodical can have multiple volumes. Volumes are mostly identified
 * by a year.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(PERIODICAL_VOLUME)
public class PeriodicalVolume extends Publication {

    private String volumeNumber;

    /**
     * Should be the same as volumeNumber, might delete later. As for now, some publications have different numbers
     * in these two attributes, so keeping both
     */
    private String volumeYear;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL_VOLUME;
    }

    @Override
    public PeriodicalVolumeDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}
