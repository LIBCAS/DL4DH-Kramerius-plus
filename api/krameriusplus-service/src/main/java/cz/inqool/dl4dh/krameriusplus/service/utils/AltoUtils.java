package cz.inqool.dl4dh.krameriusplus.service.utils;

import cz.inqool.dl4dh.alto.*;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AltoUtils {

    public static void addAltoMetadataToTokens(Page page, Alto alto) {

    }

    public static String extractPageContent(Alto page) {
        List<Alto.Layout.Page> pageElements =
                Optional.ofNullable(page)
                        .map(Alto::getLayout)
                        .map(Alto.Layout::getPage)
                        .orElse(new ArrayList<>());

        StringBuilder pageContent = new StringBuilder();

        // there is *usually* only one pageElement in every page
        for (Alto.Layout.Page pageElement : pageElements) {
            List<BlockType> textBlocks =
                    Optional.ofNullable(pageElement)
                            .map(Alto.Layout.Page::getPrintSpace)
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
                        StringType wordObject = null;

                        // every line contain multiple parts (both words and whitespaces)
                        for (var linePart : line.getStringAndSP()) {
                            if (linePart instanceof StringType) {
                                wordObject = (StringType) linePart;
                                word = wordObject.getCONTENT();

                                pageContent.append(word);
                            } else if (linePart instanceof TextBlockType.TextLine.SP) {
                                pageContent.append(" ");
                            }
                        }

                        // if last char on line is '-', remove it and do not append space
                        if (pageContent.charAt(pageContent.length() - 1) == '-') {
                            pageContent.setLength(pageContent.length() - 1);
                        } else if (wordObject == null || wordObject.getSUBSCONTENT() == null){
                            pageContent.append(" ");
                        }
                    }
                }
            }

            // remove added space after last line on page
            if (pageContent.length() > 0 && pageContent.charAt(pageContent.length() - 1) == ' ') {
                pageContent.setLength(pageContent.length() - 1);
            }
        }

        return pageContent.toString();
    }
}
