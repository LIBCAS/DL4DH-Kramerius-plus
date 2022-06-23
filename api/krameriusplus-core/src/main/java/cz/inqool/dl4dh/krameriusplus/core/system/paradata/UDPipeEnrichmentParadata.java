package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UDPipeEnrichmentParadata extends EnrichmentParadata {

    private String model;

    private String generator;

    private String[] acknowledgements;

    private String licence;

    @Override
    public ExternalSystem getExternalSystem() {
        return ExternalSystem.UD_PIPE;
    }
}
