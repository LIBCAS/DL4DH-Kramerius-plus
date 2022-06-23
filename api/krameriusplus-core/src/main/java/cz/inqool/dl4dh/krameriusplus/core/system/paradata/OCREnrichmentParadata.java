package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class OCREnrichmentParadata extends EnrichmentParadata {

    private LocalDate ocrPerformedDate;

    private String creator;

    private String softwareName;

    private String version;

    private final ExternalSystem externalSystem = ExternalSystem.OCR;
}
