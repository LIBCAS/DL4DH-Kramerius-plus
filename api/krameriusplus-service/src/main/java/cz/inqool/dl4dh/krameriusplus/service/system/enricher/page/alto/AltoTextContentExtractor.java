package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.BlockTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.PageSpaceTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.TextBlockTypeDto;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Optional;

public class AltoTextContentExtractor {

    private final StringBuilder pageContent;

    private final AltoDto alto;

    public AltoTextContentExtractor(@NonNull AltoDto alto) {
        this.alto = alto;
        pageContent = new StringBuilder();
    }

    public String extractText() {
        var pageElements = Optional.ofNullable(alto.getLayout())
                .map(AltoDto.LayoutDto::getPage)
                .orElse(new ArrayList<>());

        for (var pageElement : pageElements) {
            processPageElement(pageElement);
        }

        if (pageContent.length() > 0) {
            removeLastSpace();
        }

        return pageContent.toString();
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
