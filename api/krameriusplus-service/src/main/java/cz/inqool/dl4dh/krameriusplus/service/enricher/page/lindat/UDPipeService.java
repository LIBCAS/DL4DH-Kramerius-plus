package cz.inqool.dl4dh.krameriusplus.service.enricher.page.lindat;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.LinguisticMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Token;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.UDPipeParadata;
import cz.inqool.dl4dh.krameriusplus.domain.exception.ExternalServiceException;
import cz.inqool.dl4dh.krameriusplus.dto.LindatServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.notNull;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExternalServiceException.ErrorCode.UD_PIPE_ERROR;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class UDPipeService {

    private WebClient webClient;

    /**
     * Processes the input text content and sets tokens attribute on page.
     */
    public void tokenize(Page page) {
        UDPipeParadata udPipeParadata = new UDPipeParadata();
        udPipeParadata.setRequestSent(Instant.now());

        String pageContent = page.getContent();

        if (pageContent == null || pageContent.isEmpty()) {
            return;
        }

        LindatServiceResponse response = makeApiCall(pageContent);

        fillParadataFromResponse(udPipeParadata, response);

        page.setTokens(parseResponseToTokens(response));

        udPipeParadata.setFinishedProcessing(Instant.now());
        page.setUdPipeParadata(udPipeParadata);
    }

    private LindatServiceResponse makeApiCall(String body) {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("data", body);

        LindatServiceResponse response = webClient.post().uri(uriBuilder -> uriBuilder
                .queryParam("tokenizer", "ranges")
                .queryParam("tagger")
                .queryParam("parser")
                .build()).body(BodyInserters.fromMultipartData("data", body))
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(LindatServiceResponse.class)
                .block();

        notNull(response, () -> new ExternalServiceException(UD_PIPE_ERROR, "UDPipe did not return results"));

        if (response.getResult() != null) {
            response.setResultLines(response.getResult().split("\n"));
        }

        return response;
    }

    private void fillParadataFromResponse(UDPipeParadata udPipeParadata, LindatServiceResponse response) {
        udPipeParadata.setResponseReceived(Instant.now());
        udPipeParadata.setModel(response.getModel());
        udPipeParadata.setGenerator(getGenerator(response.getResultLines()));
        udPipeParadata.setLicence(getLicence(response.getResultLines()));
        udPipeParadata.setAcknowledgements(response.getAcknowledgements());
    }

    private String getLicence(String[] resultLines) {
        if (resultLines != null && resultLines.length > 2) {
            String thirdLine = resultLines[2];
            return thirdLine.split("=")[1].trim();
        }

        return null;
    }

    private String getGenerator(String[] resultLines) {
        if (resultLines != null && resultLines.length > 0) {
            String firstLine = resultLines[0];
            return firstLine.split("=")[1].trim();
        }

        return null;
    }

    private List<Token> parseResponseToTokens(LindatServiceResponse response) {
        String[] lines = response.getResultLines();

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

    @Resource(name = "udPipeWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
