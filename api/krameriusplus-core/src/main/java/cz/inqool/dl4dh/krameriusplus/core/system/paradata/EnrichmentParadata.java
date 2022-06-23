package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Metadata about metadata
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class EnrichmentParadata {

    protected Instant processingStarted;

    protected Instant processingFinished;

    public abstract ExternalSystem getExternalSystem();
}
