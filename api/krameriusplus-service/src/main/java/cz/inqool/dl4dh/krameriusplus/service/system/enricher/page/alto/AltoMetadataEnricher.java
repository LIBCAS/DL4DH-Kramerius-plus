package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto;

import cz.inqool.dl4dh.alto.StringType;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.AltoTokenMetadata;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.BlockTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.PageSpaceTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.TextBlockTypeDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AltoMetadataEnricher {

    private int currentTokenIndex = 0;

    private final Page page;

    public AltoMetadataEnricher(Page page) {
        this.page = page;
    }

    /**
     * Processes ALTO stored in given Page and extracts metadata about token positions from it.
     * If ALTO is {@code null}, returns.
     */
    public void enrichPage() {
        var pageElements = Optional.ofNullable(page.getAltoLayout())
                .map(AltoDto.LayoutDto::getPage)
                .orElse(new ArrayList<>());

        for (AltoDto.LayoutDto.PageDto pageElement : pageElements) {
            processPageElement(pageElement);
        }
    }

    private void processPageElement(AltoDto.LayoutDto.PageDto pageElement) {
        for (var block : Optional.ofNullable(pageElement.getPrintSpace())
                .map(PageSpaceTypeDto::getBlockTypes)
                .orElse(new ArrayList<>())) {
            processBlockElement(block);
        }
    }

    private void processBlockElement(BlockTypeDto block) {
        if (block instanceof TextBlockTypeDto) {
            processTextBlockElement((TextBlockTypeDto) block);
        }
    }

    private void processTextBlockElement(TextBlockTypeDto block) {
        for (var line : block.getTextLine()) {
            processLineElement(line);
        }
    }

    private void processLineElement(TextBlockTypeDto.TextLineDto line) {
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
