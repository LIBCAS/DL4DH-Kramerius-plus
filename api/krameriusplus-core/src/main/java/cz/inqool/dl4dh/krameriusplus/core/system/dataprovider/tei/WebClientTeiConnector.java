package cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.core.domain.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei.header.TeiHeaderFactory;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
            throw new EnrichingException(EnrichingException.ErrorCode.SERIALIZING_ERROR, e);
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        }
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
            throw new EnrichingException(EnrichingException.ErrorCode.SERIALIZING_ERROR, e);
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Override
    public File merge(String teiHeader, List<String> teiPages, TeiParams params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(List.of(MediaType.APPLICATION_XML));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("header", new MultipartInputStreamFileResource(new ByteArrayInputStream(teiHeader.getBytes(StandardCharsets.UTF_8)), "header"));

        for (String teiPage : teiPages) {
            body.add("page[]", new MultipartInputStreamFileResource(new ByteArrayInputStream(teiPage.getBytes(StandardCharsets.UTF_8)), "page.xml"));
        }

        params.getUdPipeParams().forEach(param -> body.add("UDPipe", param));
        params.getNameTagParams().forEach(param -> body.add("NameTag", param));
        params.getAltoParams().forEach(param -> body.add("ALTO", param));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.execute("/merge", HttpMethod.POST,
                restTemplate.httpEntityCallback(requestEntity),
                clientHttpResponse -> {
            File ret = File.createTempFile("download", "tmp");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });
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
