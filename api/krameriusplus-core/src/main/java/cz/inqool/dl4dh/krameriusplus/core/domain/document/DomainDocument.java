package cz.inqool.dl4dh.krameriusplus.core.domain.document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * Base class with persistent ID field. ID of the stored enriched objects should be the same as ID of non-enriched
 * object in Kramerius
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode
public class DomainDocument {

    @Id
    protected String id;

    protected Instant created;

    protected Instant updated;
}
