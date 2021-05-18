package cz.inqool.dl4dh.krameriusplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a page.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "pages")
public class Page extends DomainObject {

    @Indexed(name = "root_publication_index")
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

    /**
     * Storing it as a string for page numbers like "[1a]"
     */
    private String pageNumber;

    @Indexed
    private int pageIndex;

    private NameTagMetadata nameTagMetadata;
}
