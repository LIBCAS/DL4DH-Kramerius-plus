package cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.EXTERNAL_API_ERROR;

/**
 * @author Norbert Bodnar
 */
@Service
public class WebClientDataProvider implements DataProvider, StreamProvider {

    private WebClient webClient;

    @Override
    public Alto getAlto(String pageId) {
        return callInternal(String.format("/%s/streams/%s", pageId, StreamType.ALTO.streamId),
                new ParameterizedTypeReference<>() {});
    }

    @Override
    public String getOCR(String pageId) {
        return getNormalizedTextOcr(pageId);
    }

    @Override
    public ModsCollectionDefinition getMods(String publicationId) {
        return callInternal(String.format("/%s/streams/%s", publicationId, StreamType.MODS.streamId),
                new ParameterizedTypeReference<>() {});
    }

    @Override
    public DigitalObject getDigitalObject(String objectId) {
        return callInternal(String.format("/%s", objectId), new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<DigitalObject> getDigitalObjectsForParent(Publication parent) {
        List<DigitalObject> result = callInternal(String.format("/%s/children", parent.getId()), new ParameterizedTypeReference<>() {});

        setChildrenIndicesAndParentId(parent.getId(), result);

        return result;
    }

    private <T> T callInternal(String uri, ParameterizedTypeReference<T> typeReference) {
        try {
            return webClient.get()
                    .uri(uri)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(typeReference)
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

    public String getNormalizedTextOcr(String pageId) {
        String textOcr = callInternal(String.format("/%s/streams/%s", pageId, StreamType.TEXT_OCR.streamId),
                new ParameterizedTypeReference<>() {});

        if (textOcr != null) {
            textOcr = normalizeText(textOcr);
        }

        return textOcr;
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
