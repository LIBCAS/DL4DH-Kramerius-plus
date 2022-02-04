package cz.inqool.dl4dh.krameriusplus.service.dataprovider;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.EXTERNAL_API_ERROR;

/**
 * @author Norbert Bodnar
 */
@Service
public class WebClientDataProvider implements DataProvider, StreamProvider {

    private WebClient webClient;

    @Override
    public Alto getAlto(String pageId) {
        String altoAsString = getStreamAsString(pageId, StreamType.ALTO);

        return JAXB.unmarshal(new StringReader(altoAsString), Alto.class);
    }

    @Override
    public ModsCollectionDefinition getMods(String publicationId) {
        String modsAsString = getStreamAsString(publicationId, StreamType.MODS);

        return JAXB.unmarshal(new StringReader(modsAsString), ModsCollectionDefinition.class);
    }

    @Override
    public <T extends DigitalObject> T getDigitalObject(String objectId) {
        return callInternal(String.format("/%s", objectId));
    }

    @Override
    public <T extends DigitalObject> List<T> getDigitalObjectsForParent(Publication parent) {
        List<T> result = callInternal(String.format("/%s/children", parent.getId()));

        setChildrenIndicesAndParentId(parent.getId(), result);

        return result;
    }

    private String getStreamAsString(String digitalObjectId, StreamType streamType) {
        return callInternal(String.format("/%s/streams/%s", digitalObjectId, streamType.getStreamId()));
    }

    private <T> T callInternal(String uri) {
        try {
            return webClient.get()
                    .uri(uri)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<T>() {
                    })
                    .block();
        } catch (Exception e) {
            throw new KrameriusException(EXTERNAL_API_ERROR, e);
        }
    }

    private <T extends DigitalObject> void setChildrenIndicesAndParentId(String parentId, List<T> result) {
        int index = 0;

        if (result != null) {
            for (DigitalObject child : result) {
                child.setParentId(parentId);
                child.setIndex(index++);
            }
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

    public String getNormalizedTextOcr(String pageId) {
        String textOcr = getStreamAsString(pageId, StreamType.TEXT_OCR);

        if (textOcr != null) {
            textOcr = normalizeText(textOcr);
        }

        return textOcr;
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
