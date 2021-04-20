package cz.inqool.dl4dh.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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

    @Indexed
    private String rootId;

    private List<Token> tokens = new ArrayList<>();

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

    private int pageIndex;
}
