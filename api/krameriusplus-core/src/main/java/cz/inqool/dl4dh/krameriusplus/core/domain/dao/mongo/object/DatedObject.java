package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public abstract class DatedObject extends DomainObject {

    protected Instant created;

    protected Instant updated;

    /**
     * Logic for soft deleted not implemented
     */
    protected Instant deleted;
}
