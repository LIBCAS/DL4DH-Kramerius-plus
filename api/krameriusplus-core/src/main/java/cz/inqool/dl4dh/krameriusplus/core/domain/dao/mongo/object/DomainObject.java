package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class DomainObject {

    @Id
    @JsonAlias({ "pid" })
    protected String id;

    protected Instant created = Instant.now();
}
