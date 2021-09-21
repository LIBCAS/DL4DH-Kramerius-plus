package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.KrameriusObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.dto.DigitalObjectDto;
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
    public <T extends DigitalObjectDto<?>> T getDigitalObject(String objectId) {
        ParameterizedTypeReference<T> type = new ParameterizedTypeReference<>() {};

        try {
            return webClient.get()
                    .uri("/{objectId}", objectId)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(type)
                    .block();
        } catch (WebClientResponseException e) {
            throw new MissingObjectException(DIGITAL_OBJECT_NOT_FOUND, e);
        }
    }

    @Override
    public <T extends KrameriusObject> List<DigitalObjectDto<T>> getDigitalObjectsForParent(String parentId) {
        var result = webClient.get()
                .uri("/{parentId}/children", parentId)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<DigitalObjectDto<T>>>() {
                })
                .block();

        setChildrenIndicesAndParentId(parentId, result);

        return result;
    }

    private <T extends KrameriusObject> void setChildrenIndicesAndParentId(String parentId, List<DigitalObjectDto<T>> result) {
        int index = 0;
        if (result != null) {
            for (DigitalObjectDto<T> child : result) {
                if (child instanceof ParentAware) {
                    ParentAware parentAwareChild = (ParentAware) child;
                    parentAwareChild.setParentId(parentId);
                    parentAwareChild.setIndex(index++);
                }
            }
        }
    }

    @Resource(name = "krameriusWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
