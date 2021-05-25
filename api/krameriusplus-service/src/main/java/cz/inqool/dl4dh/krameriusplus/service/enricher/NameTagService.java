package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.NamedEntity;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Norbert Bodnar
 */
@Service
public class NameTagService {

    private final RestTemplate restTemplate;

    private final String URL;

    private final HttpHeaders headers;

    @Autowired
    public NameTagService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        URL = UriComponentsBuilder.fromHttpUrl("http://lindat.mff.cuni.cz/services/nametag/api/recognize")
                .queryParam("output", "conll")
                .queryParam("input", "vertical")
                .toUriString();
    }

    public NameTagMetadata processTokens(List<Token> tokens) {
        if (tokens.size() == 0) {
            return null;
        }

//        StringBuilder data = new StringBuilder();
//        for (Token token : tokens) {
//            data.append(token.getContent()).append(System.lineSeparator());
//        }

        //TODO: test this
        String data = tokens.stream().map(Token::getContent).collect(Collectors.joining(System.lineSeparator()));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", data);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        LindatServiceResponse response = restTemplate
                .postForEntity(URL, requestEntity, LindatServiceResponse.class)
                .getBody();


        if (response == null) {
            throw new IllegalStateException("NameTag did not return results");
        }

        String[] lines = response.getResult().split("\n");

        return processLines(lines, tokens);
    }

    private NameTagMetadata processLines(String[] lines, List<Token> tokens) {
        String word;
        String metadata;
        String[] line;
        Token token;
        NameTagMetadata nameTagMetadata = new NameTagMetadata();
        Map<String, NamedEntity> namedEntityMap = new HashMap<>();

        for (int i = 0; i < lines.length; i++) {
            line = lines[i].split("\t");
            word = line[0];
            metadata = line[1];
            token = tokens.get(i);
            if (!token.getContent().equals(word)) {
                throw new IllegalStateException("Response word not equal to input word");
            }

            if (!metadata.equals("O")) {
                token.setNameTagMetadata(metadata);

                String[] nameTagTypes = metadata.split("\\|");

                for (String nameTagType : nameTagTypes) {
                    boolean isFirst = nameTagType.startsWith("B");
                    String type = nameTagType.split("-")[1];

                    if (isFirst) {
                        NamedEntity namedEntity = new NamedEntity();
                        namedEntity.getTokens().add(token);
                        namedEntity.setEntityType(type);

                        namedEntityMap.put(type, namedEntity);
                    } else {
                        if (!namedEntityMap.containsKey(type)) {
                            throw new IllegalStateException("Named entity missing \"B\" label representing the start of a named entity tyoe");
                        }

                        NamedEntity namedEntity = namedEntityMap.get(type);
                        namedEntity.getTokens().add(token);
                    }
                }

                List<String> removeTypes = new ArrayList<>();
                for (Map.Entry<String, NamedEntity> entry : namedEntityMap.entrySet()) {
                    String type = entry.getValue().getEntityType();
                    if (Arrays.stream(nameTagTypes).noneMatch(nameTagType -> type.equals(entry.getKey()))) {
                        nameTagMetadata.add(namedEntityMap.get(entry.getKey()));
                        removeTypes.add(entry.getKey());
                    }
                }

                for (String removeType : removeTypes) {
                    namedEntityMap.remove(removeType);
                }

            } else {
                // clear out namedEntityMap
                for (NamedEntity namedEntity : namedEntityMap.values()) {
                    nameTagMetadata.add(namedEntity);
                }

                namedEntityMap = new HashMap<>();
            }
        }

        return nameTagMetadata;
    }
}
