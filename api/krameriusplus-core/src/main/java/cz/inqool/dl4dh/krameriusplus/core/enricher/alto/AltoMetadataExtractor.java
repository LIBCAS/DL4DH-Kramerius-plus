package cz.inqool.dl4dh.krameriusplus.core.enricher.alto;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.OCREnrichmentParadata;
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

    public String extractText(Alto alto) {
        AltoTextContentExtractor textExtractor = new AltoTextContentExtractor(alto);

        return textExtractor.extractText();
    }

    public OCREnrichmentParadata extractOcrParadata(Alto alto) {
        return ocrExtractor.extractOcrParadata(alto);
    }

}
