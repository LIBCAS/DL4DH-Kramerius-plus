package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.alto.ProcessingSoftwareType;
import cz.inqool.dl4dh.alto.ProcessingStepType;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class AltoOcrExtractor {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public OCREnrichmentParadata extractOcrParadata(AltoDto alto) {
        OCREnrichmentParadata ocrParadata = new OCREnrichmentParadata();

        ProcessingStepType processingStep = getProcessingStep(alto);
        if (processingStep != null) {
            ocrParadata.setOcrPerformedDate(parseOcrPerformedDate(processingStep.getProcessingDateTime()));

            ProcessingSoftwareType software = processingStep.getProcessingSoftware();
            if (software != null) {
                ocrParadata.setCreator(software.getSoftwareCreator());
                ocrParadata.setSoftwareName(software.getSoftwareName());
                ocrParadata.setVersion(software.getSoftwareVersion());
            }
        }

        return ocrParadata;
    }

    private ProcessingStepType getProcessingStep(AltoDto alto) {
        return Optional.ofNullable(alto)
                .map(AltoDto::getDescription)
                .map(Alto.Description::getOCRProcessing)
                .filter(list -> list.size() == 1)
                .map(list -> list.get(0))
                .map(Alto.Description.OCRProcessing::getOcrProcessingStep)
                .orElse(null);
    }

    private LocalDate parseOcrPerformedDate(String ocrPerformedDateString) {
        return ocrPerformedDateString != null ?
                LocalDate.parse(ocrPerformedDateString, formatter) : null;
    }
}
