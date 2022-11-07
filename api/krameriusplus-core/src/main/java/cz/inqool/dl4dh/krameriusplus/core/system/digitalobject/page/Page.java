package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.nametag.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.Token;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsMetadata;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.PAGE;

/**
 * Object representing a page.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "pages")
@TypeAlias(PAGE)
public class Page extends DigitalObject {

    private String rootId;

    private List<Token> tokens = new ArrayList<>();

    private String policy;

    /**
     * Usually page number
     */
    private String title;

    private String pageType;

    /**
     * Storing it as a string for page numbers like "[1a]"
     */
    private String pageNumber;

    private NameTagMetadata nameTagMetadata;

    private MetsMetadata metsMetadata;

    @Transient
    @JsonIgnore
    private String ndkFilePath;

    @JsonIgnore
    private String teiBodyFileId;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        pageType = (String) details.get("type");
        pageNumber = ((String) details.get("pagenumber")).strip();
    }

    @Override
    public String getModel() {
        return PAGE;
    }
}
