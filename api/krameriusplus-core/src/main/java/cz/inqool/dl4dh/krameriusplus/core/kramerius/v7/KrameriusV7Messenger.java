package cz.inqool.dl4dh.krameriusplus.core.kramerius.v7;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.config.WebClientConfig;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.*;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.StreamType.*;

//TODO: cleanup implemention extract common code from both version implementations
// TODO: implement articles + analyse "hasIntCompPart" relationships uuid:b1c91df5-ca52-458f-a291-328ae108a3cc
@Component
@ConditionalOnProperty(prefix = "system.kramerius", value = "version", havingValue = "7")
public class KrameriusV7Messenger implements KrameriusMessenger {

    private static final String STREAMS_PATH_SEGMENT = "streams";

    private WebClient webClient;

    private DigitalObjectMapperVisitor mapper;

    private static final ParameterizedTypeReference<DigitalObjectCreateDto> DIGITAL_OBJECT_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };

    private static final ParameterizedTypeReference<String> STRING_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };

    private static final ParameterizedTypeReference<ModsCollectionDefinition> MODS_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };

    @Override
    public DigitalObject getDigitalObject(String objectId) {
        return searchDigitalObject(buildUriPath("search?q=\"" + objectId + "\""))
                .accept(mapper);
    }

    @Override
    public List<DigitalObject> getDigitalObjectsForParent(String parentId) {
        DigitalObjectStructureDto digitalObjectStructureDto = getDigitalObjectStructure(buildUriPath(parentId, "info", "structure"));

        if (digitalObjectStructureDto.getChildren() == null || digitalObjectStructureDto.getChildren().getChildren() == null) {
            return new ArrayList<>();
        }

        return digitalObjectStructureDto.getChildren().getChildren()
                .stream()
                .map(objectId -> searchDigitalObject(buildUriPath("search?q=pid:\"" + objectId + "\""))
                        .accept(mapper))
                .collect(Collectors.toList());
    }

    @Override
    public Alto getAlto(String pageId) {
        String altoString = getAltoRawStream(pageId, STRING_TYPE_REF);

        return JAXB.unmarshal(new StringReader(altoString), Alto.class);
    }

    @Override
    public String getAltoString(String pageId) {
        return getAltoRawStream(pageId, STRING_TYPE_REF);
    }

    @Override
    public String getOcr(String pageId) {
        return getOcrRawStream(pageId);
    }

    @Override
    public ModsCollectionDefinition getMods(String publicationId) {
        return callInternal(
                buildUriPath(publicationId, STREAMS_PATH_SEGMENT, MODS.getStreamId()),
                MODS_TYPE_REF);
    }

    String getOcrRawStream(String pageId) {
        String raw = callInternal(
                buildUriPath(pageId, STREAMS_PATH_SEGMENT, TEXT_OCR.getStreamId()),
                STRING_TYPE_REF);

        return raw == null ? "" : raw;
    }


    private DigitalObjectStructureDto getDigitalObjectStructure(String uri) {
        try {
            return webClient.get()
                    .uri(uri)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(DigitalObjectStructureDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getRawStatusCode() == 404) {
                throw new KrameriusException(NOT_FOUND, e);
            }
            if (e.getRawStatusCode() == 403) {
                throw new KrameriusException(UNAUTHORIZED, e);
            }
            throw new KrameriusException(EXTERNAL_API_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new KrameriusException(NOT_RESPONDING, e);
        } catch (Exception e) {
            throw new KrameriusException(UNDEFINED, e);
        }
    }

    private DigitalObjectCreateDto searchDigitalObject(String uri) {
        try {
            SearchResponse response = webClient.get()
                    .uri(uri)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(SearchResponse.class)
                    .block();

            validateResponse(response);

            return response.getResponse().getDocs().get(0);
        } catch (WebClientResponseException e) {
            if (e.getRawStatusCode() == 404) {
                throw new KrameriusException(NOT_FOUND, e);
            }
            if (e.getRawStatusCode() == 403) {
                throw new KrameriusException(UNAUTHORIZED, e);
            }
            throw new KrameriusException(EXTERNAL_API_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new KrameriusException(NOT_RESPONDING, e);
        } catch (Exception e) {
            throw new KrameriusException(UNDEFINED, e);
        }
    }

    private static void validateResponse(SearchResponse response) {
        if (response == null || response.getResponseHeader() == null || response.getResponseHeader().getStatus() == null) {
            throw new KrameriusException(NO_RESPONSE, "Kramerius response header is null");
        }

        if (response.getResponse() == null) {
            throw new KrameriusException(NO_RESPONSE, "Kramerius response is null");
        }

        if (!response.getResponseHeader().getStatus().equals(0)) {
            throw new KrameriusException(BAD_STATUS_CODE, "Kramerius responded with code: " + response.getResponseHeader().getStatus());
        }

        if (!response.getResponse().getNumFound().equals(1)) {
            throw new KrameriusException(WRONG_NUMBER_OF_DOCUMENTS, "Kramerius returned wrong number of documents, expected: " + 1 + " got: " + response.getResponse().getNumFound());
        }

        if (!response.getResponse().isNumFoundExact()) {
            throw new KrameriusException(INEXACT_RESULT, "Kramerius returned an inexact result to search");
        }
    }

    private <T> T callInternal(String uri, ParameterizedTypeReference<T> typeReference) {
        try {
            return webClient.get()
                    .uri(uri)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(typeReference)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getRawStatusCode() == 404) {
                throw new KrameriusException(NOT_FOUND, e);
            }
            if (e.getRawStatusCode() == 403) {
                throw new KrameriusException(UNAUTHORIZED, e);
            }
            throw new KrameriusException(EXTERNAL_API_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new KrameriusException(NOT_RESPONDING, e);
        } catch (Exception e) {
            throw new KrameriusException(UNDEFINED, e);
        }
    }

    private <T> T getAltoRawStream(String pageId, ParameterizedTypeReference<T> typeReference) {
        try {
            return callInternal(
                    buildUriPath(pageId, STREAMS_PATH_SEGMENT, ALTO.getStreamId()),
                    typeReference);
        } catch (KrameriusException exception) {
            if (NOT_FOUND.equals(exception.getErrorCode())) {
                return callInternal(
                        buildUriPath(pageId, STREAMS_PATH_SEGMENT, ALTO.getStreamId().toLowerCase()),
                        typeReference);
            } else {
                throw exception;
            }
        }
    }



    private String buildUriPath(String... segments) {
        return UriComponentsBuilder.newInstance()
                .pathSegment(segments)
                .build()
                .toUriString();
    }

    @Resource(name = WebClientConfig.KRAMERIUS_WEB_CLIENT)
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Autowired
    public void setMapper(DigitalObjectMapperVisitor mapper) {
        this.mapper = mapper;
    }
}
