package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Represents a volume for a periodical. One periodical can have multiple volumes. Volumes are mostly identified
 * by a year.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(KrameriusModel.PERIODICAL_VOLUME)
@Document(collection = "publications")
public class PeriodicalVolume extends Publication {

    private String volumeNumber;

    /**
     * Should be the same as volumeNumber, might delete later. As for now, some publications have different numbers
     * in these two attributes, so keeping both
     */
    private String volumeYear;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        volumeNumber = (String) details.get("volumeNumber");
        volumeYear = ((String) details.get("year"));
    }
}
