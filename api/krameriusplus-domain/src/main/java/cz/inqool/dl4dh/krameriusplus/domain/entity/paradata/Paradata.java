package cz.inqool.dl4dh.krameriusplus.domain.entity.paradata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Metadata about metadata
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class Paradata {

    private final Instant created;

    private final ExternalServiceType serviceName;

    public Paradata(ExternalServiceType serviceName) {
        this.serviceName = serviceName;
        this.created = Instant.now();
    }
}
