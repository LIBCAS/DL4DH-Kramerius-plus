package cz.inqool.dl4dh.krameriusplus.service.enricher.tei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.dto.tei.TeiHeaderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;

@Service
@Slf4j
public class WebClientTeiConnector implements TeiConnector {

    private WebClient webClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public WebClientTeiConnector(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToTeiPage(Page page) {
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/page").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData("data", objectMapper.writeValueAsString(page)))
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
                    .body(BodyInserters.fromMultipartData("data",
                            objectMapper.writeValueAsString(TeiHeaderFactory.createHeaderInput(publication))))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (JsonProcessingException e) {
            log.error("Error serializing page " + publication.getId());
        }

        return "";
    }

    @Resource(name = "teiWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
