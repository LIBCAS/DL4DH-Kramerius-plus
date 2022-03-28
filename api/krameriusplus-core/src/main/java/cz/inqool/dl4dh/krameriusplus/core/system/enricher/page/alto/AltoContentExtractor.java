package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto;

import cz.inqool.dl4dh.alto.ProcessingSoftwareType;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.BlockTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.PageSpaceTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.TextBlockTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCRParadata;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class AltoContentExtractor {

    private StringBuilder pageContent;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Page page;

    public AltoContentExtractor(@NonNull Page page) {
        this.page = page;
    }

    public void enrichPage(@NonNull AltoDto alto) {
        pageContent = new StringBuilder();

        var pageElements = Optional.ofNullable(alto.getLayout())
                .map(AltoDto.LayoutDto::getPage)
                .orElse(new ArrayList<>());

        for (var pageElement : pageElements) {
            processPageElement(pageElement);
        }

        if (pageContent.length() > 0) {
            removeLastSpace();
        }

        page.setAltoLayout(alto.getLayout());
        page.setOcrParadata(extractOcrParadata(alto));
        page.setContent(pageContent.toString());
    }

    private void processPageElement(AltoDto.LayoutDto.PageDto pageElement) {
        for (var block : Optional.ofNullable(pageElement.getPrintSpace())
                .map(PageSpaceTypeDto::getBlockTypes)
                .orElse(new ArrayList<>())) {
            processBlockElement(block);
        }
    }

    private void removeLastSpace() {
        pageContent.setLength(pageContent.length() - 1);
    }

    private OCRParadata extractOcrParadata(AltoDto alto) {
        try {
            LocalDate ocrPerformedDate = null;
            try {
                String ocrPerformedDateString = alto.getDescription().getOCRProcessing().get(0).getOcrProcessingStep().getProcessingDateTime();

                ocrPerformedDate = LocalDate.parse(ocrPerformedDateString, formatter);
            } catch (Exception e) {
                // ignore
                // todo: handle better, we dont want to fail evert time that there is no date
            }

            ProcessingSoftwareType processingSoftware = alto.getDescription().getOCRProcessing().get(0).getOcrProcessingStep().getProcessingSoftware();

            String creator = processingSoftware.getSoftwareCreator();
            String softwareName = processingSoftware.getSoftwareName();
            String version = processingSoftware.getSoftwareVersion();

            OCRParadata ocrParadata = new OCRParadata();
            ocrParadata.setRequestSent(Instant.now());
            ocrParadata.setOcrPerformedDate(ocrPerformedDate);
            ocrParadata.setCreator(creator);
            ocrParadata.setSoftwareName(softwareName);
            ocrParadata.setVersion(version);

            return ocrParadata;
        } catch (IndexOutOfBoundsException exception) {
            log.error("No OCR metadata for page {}", page.getId());
            return null;
        } catch (Exception exception) {
            log.error("Error extracting paradata from OCR", exception);
            return null;
        }
    }

    private void processBlockElement(BlockTypeDto block) {
        if (block instanceof TextBlockTypeDto) {
            processTextBlockElement((TextBlockTypeDto) block);
        }
    }

    private void processTextBlockElement(TextBlockTypeDto textBlock) {
        for (var line : textBlock.getTextLine()) {
            processLineElement(line);
        }
    }

    private void processLineElement(TextBlockTypeDto.TextLineDto line) {
        boolean omitSpaceAtEndOfLine = false;

        for (Object linePart : line.getStringAndSP()) {
            if (linePart instanceof TextBlockTypeDto.TextLineDto.StringTypeDto) {
                omitSpaceAtEndOfLine = processWordAndReturnOmitSpaceFlag((TextBlockTypeDto.TextLineDto.StringTypeDto) linePart);
            } else if (linePart instanceof TextBlockTypeDto.TextLineDto.SpDto) {
                pageContent.append(" ");
            }
        }

        if (!omitSpaceAtEndOfLine) {
            pageContent.append(" ");
        }
    }

    /**
     * Processes word element, appends to pageContent and returns true if word is a
     * divided word at the end of the line and the space after the end of the line should be omitted,
     * false otherwise
     */
    private boolean processWordAndReturnOmitSpaceFlag(TextBlockTypeDto.TextLineDto.StringTypeDto wordElement) {
        if (isWordDivided(wordElement)) {
            if (isSecondPartOfDividedWord(wordElement)) {
                return false;
            }
            pageContent.append(getFullWord(wordElement));
            return true;
        } else {
            pageContent.append(wordElement.getContent());
            return false;
        }
    }

    private String getFullWord(TextBlockTypeDto.TextLineDto.StringTypeDto wordElement) {
        return wordElement.getSubscontent();
    }

    private boolean isWordDivided(TextBlockTypeDto.TextLineDto.StringTypeDto wordElement) {
        return getFullWord(wordElement) != null;
    }

    private boolean isSecondPartOfDividedWord(TextBlockTypeDto.TextLineDto.StringTypeDto wordElement) {
        return wordElement.getSubstype() != null && wordElement.getSubstype().equals("HypPart2");
    }
}
