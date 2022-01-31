package cz.inqool.dl4dh.krameriusplus.service.enricher.page.alto;

import cz.inqool.dl4dh.alto.*;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.AltoTokenMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Token;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.PageDecorator;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.lindat.NameTagPageDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AltoMetadataPageDecorator implements PageDecorator {

    private int currentTokenIndex;

    private Page page;

    private final NameTagPageDecorator nameTagPageEnricher;

    @Autowired
    public AltoMetadataPageDecorator(NameTagPageDecorator nameTagPageEnricher) {
        this.nameTagPageEnricher = nameTagPageEnricher;
    }

    @Override
    public void enrichPage(Page page) {
        nameTagPageEnricher.enrichPage(page);

        currentTokenIndex = 0;
        this.page = page;

        var pageElements = Optional.ofNullable(page.getAlto().getLayout())
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
        Integer numberOfIllustrations = page.getNumberOfIllustrations();
        page.setNumberOfIllustrations(numberOfIllustrations == null ? 1 : ++numberOfIllustrations);
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
