package cz.inqool.dl4dh.krameriusplus.service.tei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.dto.tei.TeiHeaderFactory;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
@Primary
public class WebClientTeiConnector implements TeiConnector {

    private WebClient webClient;

    private final ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    @Autowired
    public WebClientTeiConnector(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToTeiPage(Page page) {
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/page").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(page))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (JsonProcessingException e) {
            log.error("Error serializing page " + page.getId());
        }

        return "";
    }

    @Override
    public String convertToTeiHeader(Publication publication) {
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/header").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(TeiHeaderFactory.createHeaderInput(publication)))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (JsonProcessingException e) {
            log.error("Error serializing publication " + publication.getId());
        }

        return "";
    }

    @Override
    public String merge(String teiHeader, List<String> teiPages, Params params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(List.of(MediaType.APPLICATION_XML));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("header", new MultipartInputStreamFileResource(new ByteArrayInputStream(teiHeader.getBytes(StandardCharsets.UTF_8)), "header"));

        for (String teiPage : teiPages) {
            body.add("page[]", new MultipartInputStreamFileResource(new ByteArrayInputStream(teiPage.getBytes(StandardCharsets.UTF_8)), "page.xml"));
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        InputStream response = restTemplate
                .execute("/merge",
                        HttpMethod.POST,
                        restTemplate.httpEntityCallback(requestEntity),
                        HttpInputMessage::getBody);

        try {
            return response != null ? new String(response.readAllBytes(), StandardCharsets.UTF_8) : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Resource(name = "teiWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
