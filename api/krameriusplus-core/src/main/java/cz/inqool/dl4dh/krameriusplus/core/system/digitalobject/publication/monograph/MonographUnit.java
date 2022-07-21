package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.MONOGRAPH_UNIT;

/**
 * Object representing a MonographUnit. MonographUnits must contain pages directly as children
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(KrameriusModel.MONOGRAPH_UNIT)
@Document(collection = "publications")
public class MonographUnit extends Publication {

    private String partNumber;

    private String partTitle;

    @JsonProperty("donator")
    @JsonUnwrapped
    private Donators donator;

    @JsonProperty("root_pid")
    private String rootPid;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        partNumber = (String) details.get("partNumber");
        partTitle = ((String) details.get("title"));
    }
    @JsonGetter("donator")
    public List<String> getDonator() {
        return donator.getDonators();
    }
    @Override
    public String getModel() {
        return MONOGRAPH_UNIT;
    }
}
