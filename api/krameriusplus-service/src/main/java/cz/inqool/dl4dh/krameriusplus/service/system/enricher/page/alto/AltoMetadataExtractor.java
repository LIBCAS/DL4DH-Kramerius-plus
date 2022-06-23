package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AltoMetadataExtractor {

    private final AltoOcrExtractor ocrExtractor;

    @Autowired
    public AltoMetadataExtractor(AltoOcrExtractor ocrExtractor) {
        this.ocrExtractor = ocrExtractor;
    }

    public String extractText(@NonNull AltoDto alto) {
        AltoTextContentExtractor textExtractor = new AltoTextContentExtractor(alto);

        return textExtractor.extractText();
    }

    public OCREnrichmentParadata extractOcrParadata(@NonNull AltoDto alto) {
        return ocrExtractor.extractOcrParadata(alto);
    }

}
