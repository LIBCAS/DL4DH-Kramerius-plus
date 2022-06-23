package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class NameTagEnrichmentParadata extends EnrichmentParadata {

    private String model;

    private String[] acknowledgements;

    private final ExternalSystem externalSystem = ExternalSystem.NAME_TAG;
}
