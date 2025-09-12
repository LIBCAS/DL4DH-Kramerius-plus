package cz.inqool.dl4dh.krameriusplus.core.enricher.tei;

import cz.inqool.dl4dh.krameriusplus.api.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.api.export.params.AltoParam;
import cz.inqool.dl4dh.krameriusplus.api.export.params.NameTagParam;
import cz.inqool.dl4dh.krameriusplus.api.export.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.api.export.params.UdPipeParam;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.EnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.ExternalSystem;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.ProcessedBy;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.exception.EnrichingException.ErrorCode.TEI_ERROR;
import static cz.inqool.dl4dh.krameriusplus.core.config.WebClientConfig.TEI_WEB_CLIENT;

@Component
public class SyncTeiMessenger implements TeiMessenger {

    private WebClient webClient;

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
            ModsMetadata metadata = publication.getModsMetadata();
            for (Map.Entry<ExternalSystem, EnrichmentParadata> paradata : publication.getParadata().entrySet()) {
                ProcessedBy processedBy = paradata.getValue().transformToProcessedBy();
                if (processedBy.getFrom() == null && processedBy.getTo() == null && processedBy.getWhen() == null) {
                    processedBy.setFrom(publication.getCreated().toString());
                    processedBy.setTo(publication.getUpdated().toString());
                }
                metadata.getProcessedBy().add(processedBy);
            }
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/header").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(metadata))
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
    public SessionDto startMerge(InputStream teiHeader) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("header", new MultipartInputStreamFileResource(teiHeader, "header"));

            return webClient.post().uri(uriBuilder -> uriBuilder.path("/merge/prepare").build())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(SessionDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new EnrichingException("TEI Converter client request error", EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Override
    public void addMerge(String sessionId, InputStream teiPage) {
        try {
            MultiValueMap<String, Resource> map = new LinkedMultiValueMap<>();
            map.add("page[]", new MultipartInputStreamFileResource(teiPage, "page[]"));

            webClient.post().uri(uriBuilder -> uriBuilder.path("/merge/add")
                    .queryParam("session", "{sessionId}")
                    .build(sessionId))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(map))
                    .retrieve()
                    .bodyToMono(SessionDto.class)
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
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (teiParams != null) {
                map.add("NameTag", teiParams.getNameTagParams().stream().map(NameTagParam::getName).collect(Collectors.joining(",")));
                map.add("UDPipe", teiParams.getUdPipeParams().stream().map(UdPipeParam::getName).collect(Collectors.joining(",")));
                map.add("ALTO", teiParams.getAltoParams().stream().map(AltoParam::getName).collect(Collectors.joining(",")));
            }

            return webClient.post().uri(uriBuilder -> uriBuilder.path("/merge/finish")
                    .queryParam("session", "{sessionId}")
                    .build(sessionId))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(map))
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
}
