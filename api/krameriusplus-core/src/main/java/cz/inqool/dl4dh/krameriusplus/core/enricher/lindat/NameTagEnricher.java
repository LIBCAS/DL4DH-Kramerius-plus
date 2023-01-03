package cz.inqool.dl4dh.krameriusplus.core.enricher.lindat;

import cz.inqool.dl4dh.krameriusplus.api.exception.ExternalServiceException;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.NameTagEnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.core.config.WebClientConfig;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.NamedEntity;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.exception.ExternalServiceException.ErrorCode.NAME_TAG_ERROR;
import static cz.inqool.dl4dh.krameriusplus.api.exception.SystemLogDetails.LogLevel.ERROR;
import static cz.inqool.dl4dh.krameriusplus.api.exception.SystemLogDetails.LogLevel.WARNING;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.isTrue;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@Slf4j
public class NameTagEnricher {

    private WebClient webClient;

    public NameTagResponse enrichPageTokens(List<Token> tokens) {
        NameTagResponse result = new NameTagResponse();
        if (tokens.isEmpty()) {
            return result;
        }

        String pageContent = tokens
                .stream()
                .map(Token::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        LindatServiceResponseDto response = makeApiCall(pageContent);

        result.setParadata(extractParadata(response));
        result.setMetadata(processResponse(response, tokens));

        return result;
    }

    private NameTagEnrichmentParadata extractParadata(LindatServiceResponseDto response) {
        NameTagEnrichmentParadata nameTagParadata = new NameTagEnrichmentParadata();

        nameTagParadata.setModel(response.getModel());
        nameTagParadata.setAcknowledgements(response.getAcknowledgements());

        return nameTagParadata;
    }

    private LindatServiceResponseDto makeApiCall(String body) {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("data", body);

        try {
            LindatServiceResponseDto response = webClient.post().uri(uriBuilder -> uriBuilder
                    .queryParam("output", "conll")
                    .queryParam("input", "vertical")
                    .build())
                    .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(LindatServiceResponseDto.class)
                    .block();

            notNull(response, () -> new ExternalServiceException("NameTag did not return results", NAME_TAG_ERROR, ERROR));

            if (response.getResult() != null) {
                response.setResultLines(response.getResult().split("\n"));
            }

            return response;
        } catch (Exception e) {
            throw new ExternalServiceException(NAME_TAG_ERROR, e);
        }
    }

    private NameTagMetadata processResponse(LindatServiceResponseDto response, List<Token> tokens) {
        List<String[]> linesOfColumns = Arrays.stream(response.getResult().split("\n")).map(line -> line.split("\t")).collect(Collectors.toList());
        int tokenCounter = 0;

        NameTagMetadata nameTagMetadata = new NameTagMetadata();
        Map<String, NamedEntity> tempNamedEntityMap = new HashMap<>();

        for (String[] line1 : linesOfColumns) {
            String word = line1[0];
            String metadata = line1[1];
            Token token = tokens.get(tokenCounter++);

            isTrue(token.getContent().equals(word),
                    () -> new ExternalServiceException("Response word not equal to input word", NAME_TAG_ERROR, ERROR));

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
            isTrue(namedEntityMap.containsKey(type),
                    () -> new ExternalServiceException("Named entity missing \"B\" label representing the start of a named entity type",
                            NAME_TAG_ERROR, WARNING));

            NamedEntity namedEntity = namedEntityMap.get(type);
            namedEntity.getTokens().add(token);
        }
    }

    @Autowired
    public void setWebClient(@Qualifier(WebClientConfig.NAME_TAG_WEB_CLIENT) WebClient webClient) {
        this.webClient = webClient;
    }
}
