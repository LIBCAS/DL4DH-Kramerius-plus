package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.notNull;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.MISSING_STREAM;

/**
 * @author Norbert Bodnar
 */
@Service
public class StreamProvider {

    private final WebClient webClient;

    @Autowired
    public StreamProvider(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi) {
        this.webClient = WebClient.create(krameriusApi + "/search/api/v5.0/item");
    }

    public String getTextOcr(String pageId) {
        String textOcr = getStreamAsString(pageId, StreamType.TEXT_OCR);

        if (textOcr != null) {
            textOcr = normalizeText(textOcr);
        }

        return textOcr;
    }

    public Alto getAlto(String pageId) {
        String altoAsString = webClient.get()
                .uri("/{pageId}/streams/ALTO", pageId)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        notNull(altoAsString, () -> new KrameriusException(MISSING_STREAM,
                "Page with ID=" + pageId + " does not contain ALTO stream."));

        return JAXB.unmarshal(new StringReader(altoAsString), Alto.class);
    }

    public ModsCollectionDefinition getMods(String publicationId) {
        String modsAsString = webClient.get()
                .uri("/{publicationId}/streams/BIBLIO_MODS", publicationId)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        notNull(modsAsString, () -> new KrameriusException(MISSING_STREAM,
                    "Publication with ID=" + publicationId + " does not contain BIBLIO_MODS stream"));

        return JAXB.unmarshal(new StringReader(modsAsString), ModsCollectionDefinition.class);
    }

    public String getStreamAsString(String digitalObjectId, StreamType streamType) {
        return webClient.get()
                .uri("/{digitalObjectId}/streams/{streamId}", digitalObjectId, streamType.getStreamId())
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();
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
        ALTO("ALTO");

        private final String streamId;

        StreamType(String streamId) {
            this.streamId = streamId;
        }
    }
}
