package cz.inqool.dl4dh.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Base class with persistent ID field. ID of the stored enriched objects should be the same as ID of non-enriched
 * object in Kramerius
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode
public class DomainObject {

    @Id
    private String pid;
}
