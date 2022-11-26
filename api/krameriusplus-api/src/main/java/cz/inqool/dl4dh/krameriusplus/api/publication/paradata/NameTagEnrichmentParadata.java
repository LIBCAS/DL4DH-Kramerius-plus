package cz.inqool.dl4dh.krameriusplus.api.publication.paradata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameTagEnrichmentParadata extends EnrichmentParadata {

    private String model;

    private String[] acknowledgements;

    @Override
    public ExternalSystem getExternalSystem() {
        return ExternalSystem.NAME_TAG;
    }
}
