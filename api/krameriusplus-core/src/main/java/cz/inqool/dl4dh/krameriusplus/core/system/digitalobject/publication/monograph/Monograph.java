package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.MONOGRAPH;

/**
 * Object representing a Monograph. Monographs contain either monograph units as children, or directly pages. Can
 * not contain both
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(MONOGRAPH)
@Document(collection = "publications")
public class Monograph extends Publication {

    private String partNumber;

    private String partTitle;

    @JsonProperty("donator")
    @JsonUnwrapped
    private Donators donator;

    @JsonGetter("donator")
    public List<String> getDonator() {
        return donator.getDonators();
    }
    @Override
    public String getModel() {
        return MONOGRAPH;
    }
}
