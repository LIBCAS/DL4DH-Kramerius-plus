package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UDPipeEnrichmentParadata extends EnrichmentParadata {

    private String model;

    private String generator;

    private String[] acknowledgements;

    private String licence;

    private final ExternalSystem externalSystem = ExternalSystem.UD_PIPE;
}
