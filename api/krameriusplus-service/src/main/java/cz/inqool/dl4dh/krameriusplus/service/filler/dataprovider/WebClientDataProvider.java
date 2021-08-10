package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.dto.DigitalObjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
public class WebClientDataProvider implements DataProvider {

    private final WebClient webClient;

    @Autowired
    public WebClientDataProvider(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public <T extends DigitalObjectDto<?>> T getDigitalObject(String objectId) {
        ParameterizedTypeReference<T> type = new ParameterizedTypeReference<>() {};

        return webClient.get()
                .uri("/{objectId}", objectId)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(type)
                .block();
    }


    @Override
    public <T extends DomainObject> List<DigitalObjectDto<T>> getDigitalObjectsForParent(String parentId) {
        var result = webClient.get()
                .uri("/{parentId}/children", parentId)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<DigitalObjectDto<T>>>() {
                })
                .block();

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

        return result;
    }
}
