package cz.inqool.dl4dh.krameriusplus.corev2.enricher.alto;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.alto.ProcessingSoftwareType;
import cz.inqool.dl4dh.alto.ProcessingStepType;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.OCREnrichmentParadata;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AltoOcrExtractor {

    public OCREnrichmentParadata extractOcrParadata(Alto alto) {
        ProcessingStepType processingStep = getProcessingStep(alto);
        if (processingStep != null) {
            OCREnrichmentParadata ocrParadata = new OCREnrichmentParadata();

            ocrParadata.setOcrPerformedDate(processingStep.getProcessingDateTime());

            ProcessingSoftwareType software = processingStep.getProcessingSoftware();
            if (software != null) {
                ocrParadata.setCreator(software.getSoftwareCreator());
                ocrParadata.setSoftwareName(software.getSoftwareName());
                ocrParadata.setVersion(software.getSoftwareVersion());
            }

            return ocrParadata;
        }

        return null;
     }

    private ProcessingStepType getProcessingStep(Alto alto) {
        return Optional.ofNullable(alto)
                .map(Alto::getDescription)
                .map(Alto.Description::getOCRProcessing)
                .filter(list -> list.size() == 1)
                .map(list -> list.get(0))
                .map(Alto.Description.OCRProcessing::getOcrProcessingStep)
                .orElse(null);
    }
}
