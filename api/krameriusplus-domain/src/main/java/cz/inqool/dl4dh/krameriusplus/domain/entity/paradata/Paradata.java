package cz.inqool.dl4dh.krameriusplus.domain.entity.paradata;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Metadata about metadata
 */
@Getter
@Setter
public abstract class Paradata {

    protected Instant created;

    public abstract ExternalServiceType getExternalServiceType();
}
