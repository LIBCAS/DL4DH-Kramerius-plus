package cz.inqool.dl4dh.krameriusplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

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
}
