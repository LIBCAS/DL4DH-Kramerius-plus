package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.Resource;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.notNull;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.EXTERNAL_API_ERROR;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.MISSING_STREAM;

/**
 * @author Norbert Bodnar
 */
@Service
public class StreamProvider {

    private WebClient webClient;

    public String getNormalizedTextOcr(String pageId) {
        String textOcr = getStreamAsString(pageId, StreamType.TEXT_OCR);

        if (textOcr != null) {
            textOcr = normalizeText(textOcr);
        }

        return textOcr;
    }

    public Alto getAlto(String pageId) {
        String altoAsString = getStreamAsString(pageId, StreamType.ALTO);

        return JAXB.unmarshal(new StringReader(altoAsString), Alto.class);
    }

    public ModsCollectionDefinition getMods(String publicationId) {
        String modsAsString = getStreamAsString(publicationId, StreamType.MODS);

        return JAXB.unmarshal(new StringReader(modsAsString), ModsCollectionDefinition.class);
    }

    private String getStreamAsString(String digitalObjectId, StreamType streamType) {
        String stream;
        try {
            stream = webClient.get()
                    .uri("/{digitalObjectId}/streams/{streamId}", digitalObjectId, streamType.getStreamId())
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException | WebClientRequestException e) {
            throw new KrameriusException(EXTERNAL_API_ERROR, e);
        }

        notNull(stream, () -> new KrameriusException(MISSING_STREAM,
                "Object with ID=" + digitalObjectId + " does not contain " + streamType + " stream"));

        return stream;
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

    @Resource(name = "krameriusWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
