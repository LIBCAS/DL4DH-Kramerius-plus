package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UDPipeParadata extends Paradata {

    private String model;

    private String generator;

    private String[] acknowledgements;

    private String licence;

    private Instant responseReceived;

    private Instant finishedProcessing;

    @Override
    public ExternalServiceType getExternalServiceType() {
        return ExternalServiceType.UD_PIPE;
    }
}
