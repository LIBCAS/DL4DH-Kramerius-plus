package cz.inqool.dl4dh.krameriusplus.service.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.exception.MissingObjectException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.MissingObjectException.ErrorCode.DIGITAL_OBJECT_NOT_FOUND;

/**
 * @author Norbert Bodnar
 */
@Service
public class WebClientDataProvider implements DataProvider {

    private WebClient webClient;

    @Override
    public <T extends DigitalObject> T downloadDigitalObject(String objectId) {
        ParameterizedTypeReference<T> type = new ParameterizedTypeReference<>() {};

        try {
            return webClient.get()
                    .uri("/{objectId}", objectId)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(type)
                    .block();
        } catch (WebClientResponseException e) {
            throw new MissingObjectException(DIGITAL_OBJECT_NOT_FOUND, "Object with UUID=" + objectId + " not found", e);
        }
    }

    @Override
    public <T extends DigitalObject> List<T> getDigitalObjectsForParent(Publication parent) {
        List<T> result = webClient.get()
                .uri("/{parentId}/children", parent.getId())
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<T>>() {
                })
                .block();

        setChildrenIndicesAndParentId(parent.getId(), result);

        return result;
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

    @Resource(name = "krameriusWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
