package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.lindat.nametag.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.lindat.udpipe.Token;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.UDPipeParadata;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.KrameriusModel.PAGE;

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

    /**
     * TODO: cleanup incoming data, turn into enum
     */
    private String pageType;

    @Transient
    @JsonIgnore
    private String content;

    @Transient
    @JsonIgnore
    private Alto alto;

    /**
     * Storing it as a string for page numbers like "[1a]"
     */
    private String pageNumber;

    /**
     * Number of elements of type IllustrationType on page. Should represent the number of illustration on the page
     * recognized by the OCR. Information is obtained from ALTO format.
     */
    private Integer numberOfIllustrations;

    private NameTagMetadata nameTagMetadata;

    private MetsMetadata metsMetadata;

    @Transient
    @JsonIgnore
    private Path metsPath;

    private OCRParadata ocrParadata;

    private UDPipeParadata udPipeParadata;

    private NameTagParadata nameTagParadata;

    @JsonIgnore
    private String teiBody;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        pageType = (String) details.get("type");
        pageNumber = ((String) details.get("pagenumber")).strip();
    }
}
