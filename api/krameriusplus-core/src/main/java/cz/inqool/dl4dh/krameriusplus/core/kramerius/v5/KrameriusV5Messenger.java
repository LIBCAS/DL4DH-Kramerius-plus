package cz.inqool.dl4dh.krameriusplus.core.kramerius.v5;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusUtils.buildUriPath;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusUtils.normalizeText;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusUtils.setParentId;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusUtils.tryKrameriusCall;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.v5.StreamType.ALTO;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.v5.StreamType.MODS;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.v5.StreamType.TEXT_OCR;

public class KrameriusV5Messenger implements KrameriusMessenger {

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

    private final WebClient webClient;

    private final DigitalObjectMapperVisitor mapper;

    public KrameriusV5Messenger(WebClient webClient, DigitalObjectMapperVisitor mapper) {
        this.webClient = webClient;
        this.mapper = mapper;
    }

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
        String altoString = sanitizeAlto(getAltoRawStream(pageId));

        return JAXB.unmarshal(new StringReader(altoString), Alto.class);
    }

    @Override
    public String getAltoString(String pageId) {
        return sanitizeAlto(getAltoRawStream(pageId));
    }

    private String sanitizeAlto(String altoRawStream) {
        // for properly formed xml this does nothing
        return altoRawStream.substring(altoRawStream.indexOf('<'), altoRawStream.lastIndexOf('>') + 1);
    }

    @Override
    public String getOcr(String pageId) {
        return normalizeText(getOcrRawStream(pageId));
    }

    @Override
    public ModsCollectionDefinition getMods(String publicationId) {
        return callInternal(
                buildUriPath(publicationId, STREAMS_PATH_SEGMENT, MODS.getStreamId()),
                MODS_TYPE_REF);
    }

    public String getOcrRawStream(String pageId) {
        String raw = callInternal(
                buildUriPath(pageId, STREAMS_PATH_SEGMENT, TEXT_OCR.getStreamId()),
                STRING_TYPE_REF);

        return raw == null ? "" : raw;
    }

    private String getAltoRawStream(String pageId) {
        try {
            return callInternal(
                    buildUriPath(pageId, STREAMS_PATH_SEGMENT, ALTO.getStreamId()),
                    KrameriusV5Messenger.STRING_TYPE_REF);
        } catch (KrameriusException exception) {
            if (NOT_FOUND.equals(exception.getErrorCode())) {
                return callInternal(
                        buildUriPath(pageId, STREAMS_PATH_SEGMENT, ALTO.getStreamId().toLowerCase()),
                        KrameriusV5Messenger.STRING_TYPE_REF);
            } else {
                throw exception;
            }
        }
    }

    private <T> T callInternal(String uri, ParameterizedTypeReference<T> typeReference) {
            return tryKrameriusCall(() -> webClient.get()
                    .uri(uri)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(typeReference)
                    .block());
    }
}
