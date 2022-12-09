package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PAGE;

@Getter
@Setter
@TypeAlias(PAGE)
@Document(collection = "pages")
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

    private String teiBodyFileId;

    @Override
    public String getModel() {
        return PAGE;
    }
}
