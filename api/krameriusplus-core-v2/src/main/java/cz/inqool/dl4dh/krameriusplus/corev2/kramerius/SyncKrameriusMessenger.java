package cz.inqool.dl4dh.krameriusplus.corev2.kramerius;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.DigitalObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.*;
import static cz.inqool.dl4dh.krameriusplus.corev2.config.WebClientConfig.KRAMERIUS_WEB_CLIENT;
import static cz.inqool.dl4dh.krameriusplus.corev2.kramerius.SyncKrameriusMessenger.StreamType.*;

@Component
public class SyncKrameriusMessenger implements KrameriusMessenger {

    private static final ParameterizedTypeReference<Alto> ALTO_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };
    private static final ParameterizedTypeReference<ModsCollectionDefinition> MODS_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };
    private static final ParameterizedTypeReference<String> STRING_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };
    private static final ParameterizedTypeReference<DigitalObjectCreateDto> DIGITAL_OBJECT_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };
    private static final ParameterizedTypeReference<List<DigitalObjectCreateDto>> DIGITAL_OBJECT_LIST_TYPE_REF
            = new ParameterizedTypeReference<>() {
    };

    private static final String STREAMS_PATH_SEGMENT = "streams";

    private WebClient webClient;

    private DigitalObjectMapperVisitor mapper;

    @Override
    public DigitalObject getDigitalObject(String objectId) {
        return callInternal(buildUriPath(objectId), DIGITAL_OBJECT_TYPE_REF)
                .accept(mapper);
    }

    @Override
    public List<DigitalObject> getDigitalObjectsForParent(String parentId) {
        List<DigitalObject> result = callInternal(
                buildUriPath(parentId, "children"),
                DIGITAL_OBJECT_LIST_TYPE_REF)
                .stream()
                .map(createDto -> createDto.accept(mapper))
                .collect(Collectors.toList());

        setParentId(parentId, result);

        return result;
    }

    @Override
    public Alto getAlto(String pageId) {
        return getAltoRawStream(pageId, ALTO_TYPE_REF);
    }

    @Override
    public String getAltoString(String pageId) {
        return getAltoRawStream(pageId, STRING_TYPE_REF);
    }

    @Override
    public String getOcr(String pageId) {
        return normalizeText(getOcrRawStream(pageId));
    }

    @Override
    public ModsCollectionDefinition getMods(String publicationId) {
        return callInternal(
                buildUriPath(publicationId, STREAMS_PATH_SEGMENT, MODS.streamId),
                MODS_TYPE_REF);
    }

    private String getOcrRawStream(String pageId) {
        return callInternal(
                buildUriPath(pageId, STREAMS_PATH_SEGMENT, TEXT_OCR.streamId),
                STRING_TYPE_REF);
    }

    private <T> T getAltoRawStream(String pageId, ParameterizedTypeReference<T> typeReference) {
        try {
            return callInternal(
                    buildUriPath(pageId, STREAMS_PATH_SEGMENT, ALTO.streamId),
                    typeReference);
        } catch (KrameriusException exception) {
            if (NOT_FOUND.equals(exception.getErrorCode())) {
                return callInternal(
                        buildUriPath(pageId, STREAMS_PATH_SEGMENT, ALTO.streamId.toLowerCase()),
                        typeReference);
            } else {
                throw exception;
            }
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

    private String normalizeText(String textOcr) {
        return textOcr.replace("\uFEFF", "")
                .replaceAll("\\S-\r\n", "")
                .replaceAll("\\S-\n", "")
                .replaceAll("\\S–\r\n", "")
                .replaceAll("\\S–\n", "")
                .replaceAll("\r\n", " ")
                .replaceAll("\n", " ");
    }

    private String buildUriPath(String... segments) {
        return UriComponentsBuilder.newInstance()
                .pathSegment(segments)
                .build()
                .toUriString();
    }

    private void setParentId(String parentId, List<DigitalObject> result) {
        int index = 0;

        if (result != null) {
            // publications and pages will share indices
            for (DigitalObject child : result) {
                child.setParentId(parentId);
                child.setIndex(index++);
            }
        }
    }

    @Resource(name = KRAMERIUS_WEB_CLIENT)
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Autowired
    public void setMapper(DigitalObjectMapperVisitor mapper) {
        this.mapper = mapper;
    }

    @Getter
    public enum StreamType {
        TEXT_OCR("TEXT_OCR"),
        ALTO("ALTO"),
        MODS("BIBLIO_MODS");

        private final String streamId;

        StreamType(String streamId) {
            this.streamId = streamId;
        }
    }
}
