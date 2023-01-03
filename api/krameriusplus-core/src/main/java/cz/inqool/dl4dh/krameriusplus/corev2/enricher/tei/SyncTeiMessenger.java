package cz.inqool.dl4dh.krameriusplus.corev2.enricher.tei;

import cz.inqool.dl4dh.krameriusplus.api.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.api.export.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.InputStream;

import static cz.inqool.dl4dh.krameriusplus.api.exception.EnrichingException.ErrorCode.TEI_ERROR;
import static cz.inqool.dl4dh.krameriusplus.corev2.config.WebClientConfig.TEI_WEB_CLIENT;

@Component
public class SyncTeiMessenger implements TeiMessenger {

    private WebClient webClient;

    private TeiHeaderMapper headerMapper;

    @Override
    public String convertPage(Page page) {
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/page").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(page))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EnrichingException(TEI_ERROR, e);
        }
    }

    @Override
    public String convertHeader(Publication publication) {
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/header").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(headerMapper.map(publication)))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EnrichingException(TEI_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new EnrichingException("TEI Converter client request error", EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Override
    public String startMerge(InputStream teiHeader) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("header", new InputStreamResource(teiHeader));

            return webClient.post().uri(uriBuilder -> uriBuilder.path("/merge/prepare").build())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new EnrichingException("TEI Converter client request error", EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Override
    public String addMerge(String sessionId, InputStream teiPage) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("page[]", new InputStreamResource(teiPage));

            return webClient.post().uri(uriBuilder -> uriBuilder.path("/merge/add")
                    .queryParam("session", sessionId)
                    .build())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new EnrichingException("TEI Converter client request error", EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Override
    public String finishMerge(String sessionId, TeiParams teiParams) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("NameTag", teiParams.getNameTagParams());
            builder.part("UDPipe", teiParams.getUdPipeParams());
            builder.part("ALTO", teiParams.getAltoParams());

            return webClient.post().uri(uriBuilder -> uriBuilder.path("/merge/finish")
                    .queryParam("session", sessionId)
                    .build())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new EnrichingException("TEI Converter client request error", EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Autowired
    public void setWebClient(@Qualifier(TEI_WEB_CLIENT) WebClient webClient) {
        this.webClient = webClient;
    }

    @Autowired
    public void setHeaderMapper(TeiHeaderMapper headerMapper) {
        this.headerMapper = headerMapper;
    }
}
