package cz.inqool.dl4dh.krameriusplus.domain.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "model")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = KrameriusMonographDto.class, name = "monograph"),
        @JsonSubTypes.Type(value = KrameriusMonographUnitDto.class, name = "monographunit")
})
public abstract class KrameriusPublicationDto implements KrameriusModelAware {

    protected String pid;

    protected String title;

    protected List<String> collections;

    protected String policy;

    protected <T extends Publication> T toEntity(T entity) {
        entity.setPid(pid);
        entity.setTitle(title);
        entity.setCollections(collections);
        entity.setPolicy(policy);

        return entity;
    }
}
