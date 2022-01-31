package cz.inqool.dl4dh.krameriusplus.service.enricher.page.alto;

import cz.inqool.dl4dh.alto.*;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.service.dataprovider.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Service
@Order(HIGHEST_PRECEDENCE)
@Slf4j
public class AltoContentExtractor implements PageDecorator {

    private final StreamProvider streamProvider;

    private Alto alto;

    private StringBuilder pageContent;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public AltoContentExtractor(StreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    @Override
    public void enrichPage(Page page) {
        alto = streamProvider.getAlto(page.getId());
        pageContent = new StringBuilder();

        var pageElements = Optional.ofNullable(alto.getLayout())
                .map(Alto.Layout::getPage)
                .orElse(new ArrayList<>());

        for (var pageElement : pageElements) {
            processPageElement(pageElement);
        }

        if (pageContent.length() > 0) {
            removeLastSpace();
        }

        page.setAlto(alto);
        page.setOcrParadata(extractOcrParadata());
        page.setContent(pageContent.toString());
    }

    private void processPageElement(Alto.Layout.Page pageElement) {
        for (var block : Optional.ofNullable(pageElement.getPrintSpace())
                .map(PageSpaceType::getTextBlockOrIllustrationOrGraphicalElement)
                .orElse(new ArrayList<>())) {
            processBlockElement(block);
        }
    }

    private void removeLastSpace() {
        pageContent.setLength(pageContent.length() - 1);
    }

    private OCRParadata extractOcrParadata() {
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
            log.error("No OCR metadata for publication");
            return null;
        } catch (Exception exception) {
            log.error("Error extracting paradata from OCR", exception);
            return null;
        }
    }

    private void processBlockElement(BlockType block) {
        if (block instanceof TextBlockType) {
            processTextBlockElement((TextBlockType) block);
        }
    }

    private void processTextBlockElement(TextBlockType textBlock) {
        for (var line : textBlock.getTextLine()) {
            processLineElement(line);
        }
    }

    private void processLineElement(TextBlockType.TextLine line) {
        boolean omitSpaceAtEndOfLine = false;

        for (Object linePart : line.getStringAndSP()) {
            if (linePart instanceof StringType) {
                omitSpaceAtEndOfLine = processWordAndReturnOmitSpaceFlag((StringType) linePart);
            } else if (linePart instanceof TextBlockType.TextLine.SP) {
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
    private boolean processWordAndReturnOmitSpaceFlag(StringType wordElement) {
        if (isWordDivided(wordElement)) {
            if (isSecondPartOfDividedWord(wordElement)) {
                return false;
            }
            pageContent.append(getFullWord(wordElement));
            return true;
        } else {
            pageContent.append(wordElement.getCONTENT());
            return false;
        }
    }

    private String getFullWord(StringType wordElement) {
        return wordElement.getSUBSCONTENT();
    }

    private boolean isWordDivided(StringType wordElement) {
        return getFullWord(wordElement) != null;
    }

    private boolean isSecondPartOfDividedWord(StringType wordElement) {
        return wordElement.getSUBSTYPE() != null && wordElement.getSUBSTYPE().equals("HypPart2");
    }
}
