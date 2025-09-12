package cz.inqool.dl4dh.krameriusplus.api.publication.paradata;

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

    @Override
    public ProcessedBy transformToProcessedBy() {
        return new ProcessedBy("UDPipe", null, null, null, null, "UDPipe, model: "+model);
    }
}
