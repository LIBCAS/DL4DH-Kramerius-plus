package cz.inqool.dl4dh.krameriusplus.domain.entity;

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
}
