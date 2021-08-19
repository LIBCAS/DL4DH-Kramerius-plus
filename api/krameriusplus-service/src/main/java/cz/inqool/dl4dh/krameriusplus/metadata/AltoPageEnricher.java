package cz.inqool.dl4dh.krameriusplus.metadata;

import cz.inqool.dl4dh.alto.*;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.AltoTokenMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Token;
import cz.inqool.dl4dh.krameriusplus.paradata.OCRParadata;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Enriches page with positional metadata obtained from Alto format.
 */
@Slf4j
public class AltoPageEnricher {

    private final Alto alto;
    private int currentTokenIndex;
    private Page page;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");

    public AltoPageEnricher(Alto alto) {
        this.alto = alto;
    }

    public OCRParadata extractOcrParadata() {
        try {
            String ocrPerformedDateString = alto.getDescription().getOCRProcessing().get(0).getOcrProcessingStep().getProcessingDateTime();

            LocalDate ocrPerformedDate = LocalDate.parse(ocrPerformedDateString, formatter);
            ProcessingSoftwareType processingSoftware = alto.getDescription().getOCRProcessing().get(0).getOcrProcessingStep().getProcessingSoftware();

            String creator = processingSoftware.getSoftwareCreator();
            String softwareName = processingSoftware.getSoftwareName();
            String version = processingSoftware.getSoftwareVersion();

            return new OCRParadata(ocrPerformedDate, creator, softwareName, version);
        } catch (Exception exception) {
            throw new IllegalStateException("Error extracting paradata from OCR", exception);
        }
    }

    /**
     * Works only if the content of the page was extracted from Alto and word concatenation at the end of lines
     * was performed only if the information about word division is stored in Alto. If pageContent was obtained from
     * OCR, or it was somehow altered, this will not work, because token.getContent may return a longer word, than
     * the word obtained from ALTO and therefore it will never match (contents from Tokens are concatenated until it
     * is matched against the content in ALTO element.
     */
    public void enrichWithAlto(Page page) {
        currentTokenIndex = 0;
        this.page = page;

        var pageElements = Optional.ofNullable(alto.getLayout())
                .map(Alto.Layout::getPage)
                .orElse(new ArrayList<>());

        for (var pageElement : pageElements) {
            processPageElement(pageElement);
        }
    }

    private void processPageElement(Alto.Layout.Page pageElement) {
        for (var block : Optional.ofNullable(pageElement.getPrintSpace())
                .map(PageSpaceType::getTextBlockOrIllustrationOrGraphicalElement)
                .orElse(new ArrayList<>())) {
            processBlockElement(block);
        }
    }

    private void processBlockElement(BlockType block) {
        if (block instanceof TextBlockType) {
            processTextBlockElement((TextBlockType) block);
        } else if (block instanceof IllustrationType) {
            processIllustrationElement((IllustrationType) block);
        }
    }

    private void processIllustrationElement(IllustrationType block) {
        page.setNumberOfIllustrations(page.getNumberOfIllustrations() + 1);
    }

    private void processTextBlockElement(TextBlockType block) {
        for (var line : block.getTextLine()) {
            processLineElement(line);
        }
    }

    private void processLineElement(TextBlockType.TextLine line) {
        List<StringType> wordParts = line.getStringAndSP()
                .stream()
                .filter(linePart -> linePart instanceof StringType)
                .map(linePart -> (StringType) linePart)
                .collect(Collectors.toList());

        for (StringType word : wordParts) {
            if (isWordSecondPartOfDividedWord(word)) {
                continue;
            }

            String altoWord = getFullWord(word);
            String tokenWord = "";


            List<Token> affectedTokens = new ArrayList<>();

            while (!altoWord.equals(tokenWord)) {

                // in cases when the content from the first Token is actually longer than the content from alto word.
                // This happens only in rare cases and is usually caused by errors in OCR scan. Words in ALTO are
                // almost always longer than words from Tokens, because a word in ALTO can contain the
                // word + punctuation, while the punctuation in Tokens is always a separate Token.
                if (currentTokenIndex >= page.getTokens().size()) {
                    return;
                }

                Token currentToken = page.getTokens().get(currentTokenIndex++);
                affectedTokens.add(currentToken);
                tokenWord += currentToken.getContent();
            }

            AltoTokenMetadata altoMetadata = new AltoTokenMetadata(
                    word.getHEIGHT(),
                    word.getWIDTH(),
                    word.getVPOS(),
                    word.getHPOS());

            for (Token token : affectedTokens) {
                token.setAltoMetadata(altoMetadata);
            }
        }
    }

    private String getFullWord(StringType word) {
        return word.getSUBSCONTENT() != null ? word.getSUBSCONTENT() : word.getCONTENT();
    }

    private boolean isWordSecondPartOfDividedWord(StringType word) {
        return word.getSUBSTYPE() != null && word.getSUBSTYPE().equals("HypPart2");
    }
}
