package cz.inqool.dl4dh.krameriusplus.domain.service;

import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPageDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Token;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
public interface TokenizerService {

    /**
     * Processes the input text content and returns a list of tokens.
     * @param content String content to tokenize
     * @return list of Tokens produced by the external service. Tokens might contain additional metadata
     */
    List<Token> tokenize(String content);

    /**
     * Processes textContent of the page and sets its {@code List<Token> tokens} field
     * @param krameriusPageDto
     */
    Page tokenizePage(KrameriusPageDto krameriusPageDto);

    /**
     * Processes textContent of multiple pages.
     * @param krameriusPageDtos list of pages, every page's {@code List<Token> tokens} field will be overridden
     */
    List<Page> tokenizePages(List<KrameriusPageDto> krameriusPageDtos);

    /**
     * Processes textContent of multiple pages in a bulk operation.
     * @param krameriusPageDtos list of pages, every page's {@code List<Token> tokens} field will be overridden
     */
    List<Page> tokenizePagesBulk(List<KrameriusPageDto> krameriusPageDtos);
}
