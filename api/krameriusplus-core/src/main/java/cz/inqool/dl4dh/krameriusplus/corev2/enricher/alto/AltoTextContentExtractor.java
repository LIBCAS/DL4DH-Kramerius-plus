package cz.inqool.dl4dh.krameriusplus.corev2.enricher.alto;

import cz.inqool.dl4dh.alto.*;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Optional;

public class AltoTextContentExtractor {

    private final StringBuilder pageContent;

    private final Alto alto;

    public AltoTextContentExtractor(@NonNull Alto alto) {
        this.alto = alto;
        pageContent = new StringBuilder();
    }

    public String extractText() {
        var pageElements = Optional.ofNullable(alto.getLayout())
                .map(Alto.Layout::getPage)
                .orElse(new ArrayList<>());

        for (var pageElement : pageElements) {
            processPageElement(pageElement);
        }

        if (pageContent.length() > 0) {
            removeLastSpace();
        }

        return pageContent.toString();
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
        return wordElement.getSUBSTYPE()!= null && wordElement.getSUBSTYPE().equals("HypPart2");
    }
}
