package cz.inqool.dl4dh.krameriusplus.core.enricher.alto;

import cz.inqool.dl4dh.alto.*;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.AltoTokenMetadata;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Token;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AltoMetadataEnricher {

    /**
     * Processes ALTO stored in given Page and extracts metadata about token positions from it.
     * If ALTO is {@code null}, returns.
     */
    public void enrichPageTokens(Page page, Alto.Layout alto) {
        AltoEnricherState state = new AltoEnricherState(page, alto);

        state.enrichPageTokens();
    }

    private static class AltoEnricherState {
        private int currentTokenIndex = 0;
        private final Page page;
        private final Alto.Layout altoLayout;

        private AltoEnricherState(Page page, Alto.Layout layout) {
            this.page = page;
            this.altoLayout = layout;
        }

        private void enrichPageTokens() {
            var pageElements = Optional.ofNullable(altoLayout)
                    .map(Alto.Layout::getPage)
                    .orElse(new ArrayList<>());

            for (Alto.Layout.Page pageElement : pageElements) {
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
            }
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
}
