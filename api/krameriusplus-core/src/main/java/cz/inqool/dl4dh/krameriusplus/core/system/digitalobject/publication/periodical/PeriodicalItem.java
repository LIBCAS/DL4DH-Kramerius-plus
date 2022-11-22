package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.PERIODICAL_ITEM;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(KrameriusModel.PERIODICAL_ITEM)
@Document(collection = "publications")
public class PeriodicalItem extends Publication {

    private String date;

    private String issueNumber;

    private String partNumber;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        date = (String) details.get("date");
        issueNumber = (String) details.get("issueNumber");
        partNumber = (String) details.get("partNumber");
    }

    @Override
    public String getModel() {
        return PERIODICAL_ITEM;
    }
}
