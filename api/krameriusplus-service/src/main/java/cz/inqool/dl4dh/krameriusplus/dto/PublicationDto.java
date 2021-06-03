package cz.inqool.dl4dh.krameriusplus.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public abstract class PublicationDto<T extends Publication> extends DigitalObjectDto<T> {

    protected String title;

    protected List<String> collections;

    protected String policy;

    protected T toEntity(T entity) {
        entity = super.toEntity(entity);
        entity.setTitle(title);
        entity.setCollections(collections);
        entity.setPolicy(policy);

        return entity;
    }
}
