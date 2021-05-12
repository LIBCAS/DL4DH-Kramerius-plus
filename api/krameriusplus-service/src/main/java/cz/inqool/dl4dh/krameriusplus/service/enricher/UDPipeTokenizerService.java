package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPageDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.LinguisticMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Token;
import cz.inqool.dl4dh.krameriusplus.domain.exception.UDPipeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.UDPipeException.ErrorCode.EXTERNAL_SERVICE_ERROR;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class UDPipeTokenizerService {

    private final RestTemplate restTemplate;

    private final String URL;

    private final HttpHeaders headers;

    private long UDPipeExecutionTime;

    @Autowired
    public UDPipeTokenizerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        URL = UriComponentsBuilder.fromHttpUrl("http://lindat.mff.cuni.cz/services/udpipe/api/process")
                .queryParam("tokenizer", "ranges")
                .queryParam("tagger")
                .queryParam("parser")
                .toUriString();
    }

    /**
     * Processes the input text content and returns a list of tokens.
     * @param content String content to tokenize
     * @return list of Tokens produced by the external service. Tokens might contain additional metadata
     */
    public List<Token> tokenize(String content) {
        List<Token> result = new ArrayList<>();

        if (content == null || content.isEmpty()) {
            return result;
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", content);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        long start = System.currentTimeMillis();
        LindatServiceResponse response = restTemplate
                .postForEntity(URL, requestEntity, LindatServiceResponse.class)
                .getBody();
        UDPipeExecutionTime += System.currentTimeMillis() - start;

        if (response == null) {
            throw new UDPipeException(EXTERNAL_SERVICE_ERROR, "UDPipe did not return results");
        }

        return processPageResponse(response.getResult());
    }

    /**
     * Processes textContent of the page and sets its {@code List<Token> tokens} field
     * @param krameriusPageDto dto from Kramerius with text content
     */
    public Page tokenizePage(KrameriusPageDto krameriusPageDto) {
        Page page = krameriusPageDto.toEntity();
        page.setTokens(tokenize(krameriusPageDto.getTextOcr()));
        return page;
    }

    /**
     * Processes textContent of multiple pages.
     * @param krameriusPageDtos list of pages, every page's {@code List<Token> tokens} field will be overridden
     */
    public List<Page> tokenizePages(List<KrameriusPageDto> krameriusPageDtos) {
        List<Page> result = new ArrayList<>();

        for (KrameriusPageDto krameriusPageDto : krameriusPageDtos) {
            result.add(tokenizePage(krameriusPageDto));
        }

        return result;
    }

    /**
     * Processes textContent of multiple pages in a bulk operation.
     * @param krameriusPageDtos list of pages, every page's {@code List<Token> tokens} field will be overridden
     */
    public List<Page> tokenizePagesBulk(List<KrameriusPageDto> krameriusPageDtos) {
        log.info("Processing pages in UDPipe in bulk");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        String bodyContent = krameriusPageDtos
                .stream()
                .map(KrameriusPageDto::getTextOcr)
                .collect(Collectors.joining(
                        System.lineSeparator() +
                                System.lineSeparator() +
                                "\\newline" +
                                System.lineSeparator() +
                                System.lineSeparator()));

        body.set("data", bodyContent);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        log.info("Sending request to UDPipe");
        long start = System.currentTimeMillis();
        LindatServiceResponse response = restTemplate
                .postForEntity(URL, requestEntity, LindatServiceResponse.class).getBody();
        long executionTime = System.currentTimeMillis() - start;
        log.info("UDPipe execution time in bulk: " + executionTime/(double) 1000 + "s");

        if (response == null) {
            throw new UDPipeException(EXTERNAL_SERVICE_ERROR, "UDPipe did not return results");
        }

        return processBulkResponse(krameriusPageDtos, response.getResult());
    }

    private List<Page> processBulkResponse(List<KrameriusPageDto> krameriusPageDtos, String responseBody) {
        log.info("Processing response");
        String[] lines = responseBody.split("\n");

        int tokenIndex = 0;
        int skipNext = 0;
        String previousMiscColumn = "";
        String previousContent = "";

        boolean newPage = true;
        Page page = null;
        int pageIndex = 0;

        List<Token> resultTokens = new ArrayList<>();
        List<Page> resultPages = new ArrayList<>();

        for (String line : lines) {
            if (skipNext > 0) {
                skipNext--;
                continue;
            }

            if (newPage) {
                if (page != null) {
                    page.setTokens(resultTokens);
                    resultPages.add(page);
                    resultTokens = new ArrayList<>();
                }
                newPage = false;
                page = krameriusPageDtos.get(pageIndex++).toEntity();
                tokenIndex = 0;
            }

            if (line.equals("# text = \\newline")) {
                newPage = true;
                skipNext = 2;
            }

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] columns = line.split("\t");

            Token token = new Token();
            token.setContent(columns[1]);

            LinguisticMetadata metadata = new LinguisticMetadata();

            try {
                metadata.setPosition(Integer.parseInt(columns[0]));
            } catch (NumberFormatException e) {
                // metadata for this word are split into two lines
                log.warn("Error parsing output line number on: " + line);
                previousMiscColumn = columns[9];
                previousContent = columns[1];
                continue;
            }

            metadata.setLemma(parseColumn(columns[2]));
            metadata.setUPosTag(parseColumn(columns[3]));
            metadata.setXPosTag(parseColumn(columns[4]));
            metadata.setFeats(parseColumn(columns[5]));
            metadata.setHead(parseColumn(columns[6]));
            metadata.setDepRel(parseColumn(columns[7]));

            if (!previousMiscColumn.isEmpty()) {
                token.setContent(previousContent);
                previousContent = "";
                metadata.setMisc(previousMiscColumn);
                parseMisc(token, previousMiscColumn);
                previousMiscColumn = "";
                skipNext = 1;
            } else {
                metadata.setMisc(parseColumn(columns[9]));
                parseMisc(token, columns[9]);
            }

            token.setLinguisticMetadata(metadata);

            token.setTokenIndex(tokenIndex++);
            resultTokens.add(token);
        }

        if (page != null) {
            page.setTokens(resultTokens);
            resultPages.add(page);
        }

        return resultPages;
    }


    private List<Token> processPageResponse(String responseBody) {
        String[] lines = responseBody.split("\n");

        int tokenIndex = 0;
        boolean skipNext = false;
        String previousMiscColumn = "";
        String previousContent = "";

        List<Token> result = new ArrayList<>();

        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            if (skipNext) {
                skipNext = false;
                continue;
            }

            String[] columns = line.split("\t");

            Token token = new Token();
            token.setContent(columns[1]);

            LinguisticMetadata metadata = new LinguisticMetadata();

            try {
                metadata.setPosition(Integer.parseInt(columns[0]));
            } catch (NumberFormatException e) {
                // metadata for this word are split into two lines
                log.warn("Error parsing output line number on: " + line);
                previousMiscColumn = columns[9];
                previousContent = columns[1];
                continue;
            }

            metadata.setLemma(parseColumn(columns[2]));
            metadata.setUPosTag(parseColumn(columns[3]));
            metadata.setXPosTag(parseColumn(columns[4]));
            metadata.setFeats(parseColumn(columns[5]));
            metadata.setHead(parseColumn(columns[6]));
            metadata.setDepRel(parseColumn(columns[7]));

            if (!previousMiscColumn.isEmpty()) {
                token.setContent(previousContent);
                previousContent = "";
                metadata.setMisc(previousMiscColumn);
                parseMisc(token, previousMiscColumn);
                previousMiscColumn = "";
                skipNext = true;
            } else {
                metadata.setMisc(parseColumn(columns[9]));
                parseMisc(token, columns[9]);
            }

            token.setLinguisticMetadata(metadata);

            token.setTokenIndex(tokenIndex++);
            result.add(token);
        }

        return result;
    }

    private void parseMisc(Token token, String miscColumn) {
        String[] miscs = miscColumn.split("\\|");

        for (String misc : miscs) {
            if (misc.startsWith("TokenRange")) {
                String[] tokenRanges = misc.split("=")[1].split(":");
                token.setStartOffset(Integer.parseInt(tokenRanges[0]));
                token.setEndOffset(Integer.parseInt(tokenRanges[1]));
                break;
            }
        }
    }

    private String parseColumn(String columnValue) {
        if (columnValue == null || columnValue.isEmpty()) {
            log.error("Missing column value in response");
            return null;
        }

        if (columnValue.equals("_")) {
            return null;
        }

        return columnValue;
    }
}
