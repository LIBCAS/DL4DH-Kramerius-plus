package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OCREnrichmentParadata extends EnrichmentParadata {

    private String ocrPerformedDate;

    private String creator;

    private String softwareName;

    private String version;

    @Override
    public ExternalSystem getExternalSystem() {
        return ExternalSystem.OCR;
    }
}
