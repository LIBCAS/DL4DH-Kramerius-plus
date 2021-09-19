package cz.inqool.dl4dh.krameriusplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public abstract class Publication extends DomainObject {

    private String title;

    private List<String> collections;

    private String policy;

    private ModsMetadata modsMetadata;

    @JsonIgnore
    private String teiHeader;

    /**
     * Returns a list of children publications. If a publication has no children, it returns an empty list.
     * @return
     */
    @JsonIgnore
    public abstract List<? extends Publication> getChildren();
}
