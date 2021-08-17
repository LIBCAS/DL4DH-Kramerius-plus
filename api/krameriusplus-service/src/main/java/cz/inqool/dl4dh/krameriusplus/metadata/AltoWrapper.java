package cz.inqool.dl4dh.krameriusplus.metadata;

import cz.inqool.dl4dh.alto.*;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.AltoTokenMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Token;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AltoWrapper {

    private final Alto alto;
    private int currentTokenIndex;
    private Page page;

    public AltoWrapper(@NonNull Alto alto) {
        this.alto = alto;
    }

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
                // in cases when the content from Token is actually longer than the content from alto. This happens
                // only in rare cases and is usually caused by errors in OCR scan. Words in ALTO are almost always
                // longer than words from Tokens, because a word in ALTO can contain the word + punctuation,
                // while the punctuation in Tokens is always a separate Token.
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

    public String extractPageContent() {
        List<Alto.Layout.Page> pageElements =
                Optional.ofNullable(alto.getLayout())
                        .map(Alto.Layout::getPage)
                        .orElse(new ArrayList<>());

        StringBuilder pageContent = new StringBuilder();

        // there is *usually* only one pageElement in every page
        for (Alto.Layout.Page pageElement : pageElements) {
            List<BlockType> textBlocks =
                    Optional.ofNullable(pageElement.getPrintSpace())
                            .map(PageSpaceType::getTextBlockOrIllustrationOrGraphicalElement)
                            .orElse(new ArrayList<>());

            // page can contain multiple blocks of texts
            for (BlockType block : textBlocks) {
                if (block instanceof TextBlockType) {
                    TextBlockType textBlock = (TextBlockType) block;

                    List<TextBlockType.TextLine> lines = textBlock.getTextLine();

                    // each text block can contain multiple lines
                    for (TextBlockType.TextLine line : lines) {
                        String word;
                        StringType wordElement;
                        boolean appendSpaceAtEndOfLine = true;

                        // every line contain multiple parts (words and whitespaces)
                        for (Object linePart : line.getStringAndSP()) {
                            if (linePart instanceof StringType) {
                                wordElement = (StringType) linePart;
                                if (wordElement.getSUBSTYPE() != null && wordElement.getSUBSTYPE().equals("HypPart2")) {
                                    continue;
                                }

                                if (wordElement.getSUBSCONTENT() != null) {
                                    appendSpaceAtEndOfLine = false;
                                    word = wordElement.getSUBSCONTENT();
                                } else {
                                    word = wordElement.getCONTENT();
                                }

                                pageContent.append(word);
                            } else if (linePart instanceof TextBlockType.TextLine.SP) {
                                pageContent.append(" ");
                            }
                        }

                        if (appendSpaceAtEndOfLine) {
                            pageContent.append(" ");
                        }
                    }
                }
            }
        }

        if (pageContent.length() > 0) {
            pageContent.setLength(pageContent.length() - 1);
        }
        return pageContent.toString();
    }
}
