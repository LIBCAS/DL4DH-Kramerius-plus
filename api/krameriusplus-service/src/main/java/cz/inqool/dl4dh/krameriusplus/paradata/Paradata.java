package cz.inqool.dl4dh.krameriusplus.paradata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * Metadata about metadata
 */
@AllArgsConstructor
public class Paradata {

    private final Instant created = Instant.now();

    @Getter
    private final ExternalService serviceName;
}
