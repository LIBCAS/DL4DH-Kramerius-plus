package cz.inqool.dl4dh.krameriusplus.service.enricher.page.lindat;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.NamedEntity;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Token;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.NameTagParadata;
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
import java.util.*;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.isTrue;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.notNull;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExternalServiceException.ErrorCode.NAME_TAG_ERROR;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class NameTagService {

    private WebClient webClient;

    public void processTokens(Page page) {
        List<Token> tokens = page.getTokens();

        if (tokens.isEmpty()) {
            return;
        }

        NameTagParadata nameTagParadata = new NameTagParadata();
        nameTagParadata.setRequestSent(Instant.now());

        String pageContent = tokens
                .stream()
                .map(Token::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        LindatServiceResponse response = makeApiCall(pageContent);

        fillParadataFromResponse(nameTagParadata, response);

        page.setNameTagMetadata(processResponse(response, tokens));
        nameTagParadata.setFinishedProcessing(Instant.now());

        page.setNameTagParadata(nameTagParadata);
    }

    private void fillParadataFromResponse(NameTagParadata nameTagParadata, LindatServiceResponse response) {
        nameTagParadata.setResponseReceived(Instant.now());
        nameTagParadata.setModel(response.getModel());
        nameTagParadata.setAcknowledgements(response.getAcknowledgements());
    }

    private LindatServiceResponse makeApiCall(String body) {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("data", body);

        LindatServiceResponse response =  webClient.post().uri(uriBuilder -> uriBuilder
                .queryParam("output", "conll")
                .queryParam("input", "vertical")
                .build())
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(LindatServiceResponse.class)
                .block();

        notNull(response, () -> new ExternalServiceException(NAME_TAG_ERROR, "NameTag did not return results"));

        if (response.getResult() != null) {
            response.setResultLines(response.getResult().split("\n"));
        }

        return response;
    }

    private NameTagMetadata processResponse(LindatServiceResponse response, List<Token> tokens) {
        List<String[]> linesOfColumns = Arrays.stream(response.getResult().split("\n")).map(line -> line.split("\t")).collect(Collectors.toList());
        int tokenCounter = 0;

        NameTagMetadata nameTagMetadata = new NameTagMetadata();
        Map<String, NamedEntity> tempNamedEntityMap = new HashMap<>();

        for (String[] line1 : linesOfColumns) {
            String word = line1[0];
            String metadata = line1[1];
            Token token = tokens.get(tokenCounter++);

            isTrue(token.getContent().equals(word),
                    () -> new ExternalServiceException(NAME_TAG_ERROR, "Response word not equal to input word"));

            if (!metadata.equals("O")) {
                token.setNameTagMetadata(metadata);

                String[] recognizedNamedEntityTypes = metadata.split("\\|");

                for (String entityType : recognizedNamedEntityTypes) {
                    processNameTagType(tempNamedEntityMap, entityType, token);
                }

                List<String> removeTypes = new ArrayList<>();
                for (Map.Entry<String, NamedEntity> entry : tempNamedEntityMap.entrySet()) {
                    String type = entry.getValue().getEntityType();
                    if (Arrays.stream(recognizedNamedEntityTypes).noneMatch(nameTagType -> type.equals(entry.getKey()))) {
                        nameTagMetadata.add(tempNamedEntityMap.get(entry.getKey()));
                        removeTypes.add(entry.getKey());
                    }
                }

                for (String removeType : removeTypes) {
                    tempNamedEntityMap.remove(removeType);
                }
            } else {
                // clear out tempNamedEntityMap
                for (NamedEntity namedEntity : tempNamedEntityMap.values()) {
                    try {
                        nameTagMetadata.add(namedEntity);
                    } catch (IllegalArgumentException e) {
                        log.error("Cannot construct NamedEntityType enum from value: " + namedEntity.getEntityType());
                    }
                }

                tempNamedEntityMap = new HashMap<>();
            }
        }

        return nameTagMetadata;
    }

    private void processNameTagType(Map<String, NamedEntity> namedEntityMap, String entityType, Token token) {
        boolean isFirst = entityType.startsWith("B");
        String type = entityType.split("-")[1];

        if (isFirst) {
            NamedEntity namedEntity = new NamedEntity();
            namedEntity.getTokens().add(token);
            namedEntity.setEntityType(type);

            namedEntityMap.put(type, namedEntity);
        } else {
            isTrue(namedEntityMap.containsKey(type), () -> new ExternalServiceException(NAME_TAG_ERROR,
                    "Named entity missing \"B\" label representing the start of a named entity type"));

            NamedEntity namedEntity = namedEntityMap.get(type);
            namedEntity.getTokens().add(token);
        }
    }

    @Resource(name = "nameTagWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
