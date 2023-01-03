package cz.inqool.dl4dh.krameriusplus.api.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
public abstract class DomainDocumentDto {

    protected String id;

    protected Instant created;

    protected Instant updated;
}
