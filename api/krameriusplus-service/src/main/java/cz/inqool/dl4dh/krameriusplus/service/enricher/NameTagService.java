package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public void processPage(Page page) {
        if (page.getTokens().size() == 0) {
            return;
        }

        StringBuilder data = new StringBuilder();
        for (Token token : page.getTokens()) {
            data.append(token.getContent()).append(System.lineSeparator());
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", data.toString());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        LindatServiceResponse response = restTemplate
                .postForEntity(URL, requestEntity, LindatServiceResponse.class)
                .getBody();


        if (response == null) {
            throw new IllegalStateException("NameTag did not return results");
        }

        String[] lines = response.getResult().split("\n");

        processLines(lines, page);
    }

    private void processLines(String[] lines, Page page) {
        String word;
        String metadata;
        String[] line;
        Token token;

        for (int i = 0; i < lines.length; i++) {
            line = lines[i].split("\t");
            word = line[0];
            metadata = line[1];
            token = page.getTokens().get(i);
            if (!token.getContent().equals(word)) {
                throw new IllegalStateException("Response word not equal to input word");
            }

            if (!metadata.equals("O")) {
                token.setNameTagMetadata(metadata);
            }
        }
    }
}
