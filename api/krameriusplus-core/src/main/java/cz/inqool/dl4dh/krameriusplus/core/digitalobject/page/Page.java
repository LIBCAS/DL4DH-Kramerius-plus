package cz.inqool.dl4dh.krameriusplus.core.digitalobject.page;

import com.fasterxml.jackson.annotation.JsonAlias;
import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.PAGE;


@Getter
@Setter
@TypeAlias(PAGE)
@Document(collection = "pages")
public class Page extends DigitalObject {

    private List<Token> tokens = new ArrayList<>();

    private String policy;

    @JsonAlias({"page.type"})
    private String pageType;

    /**
     * Storing it as a string for page numbers like "[1a]"
     */
    @JsonAlias({"page.number"})
    private String pageNumber;

    private NameTagMetadata nameTagMetadata;

    private MetsMetadata metsMetadata;

    private String teiBodyFileId;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PAGE;
    }

    public PageDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}
