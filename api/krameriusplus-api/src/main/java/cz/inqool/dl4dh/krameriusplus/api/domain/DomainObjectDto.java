package cz.inqool.dl4dh.krameriusplus.api.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public abstract class DomainObjectDto {

    /**
     * NotNull only if operation is not update
     * Validation is delegated to the persistence layer
     */
    protected String id;

}
