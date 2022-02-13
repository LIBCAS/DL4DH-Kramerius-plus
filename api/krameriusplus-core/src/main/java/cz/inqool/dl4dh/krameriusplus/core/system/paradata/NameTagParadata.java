package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class NameTagParadata extends Paradata {

    private String model;

    private String[] acknowledgements;

    private Instant responseReceived;

    private Instant finishedProcessing;

    @Override
    public ExternalServiceType getExternalServiceType() {
        return ExternalServiceType.NAME_TAG;
    }
}
