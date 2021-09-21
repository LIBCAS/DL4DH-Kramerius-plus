package cz.inqool.dl4dh.krameriusplus.domain.dao.cascade;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PublicationRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class CascadeCallback implements ReflectionUtils.FieldCallback {

    private Object source;
    private PublicationRepository publicationRepository;

    public CascadeCallback(final Object source, final PublicationRepository publicationRepository) {
        this.source = source;
        this.publicationRepository = publicationRepository;
    }

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
            final Object fieldValue = field.get(getSource());

            if (fieldValue != null && isValidCollection(fieldValue)) {
                Collection<Publication> publications = (Collection<Publication>) fieldValue;
                publicationRepository.saveAll(publications);
            }
        }
    }

    private boolean isValidCollection(Object fieldValue) {
        return isCollection(fieldValue) && containsPublications(fieldValue);
    }

    private boolean containsPublications(Object fieldValue) {
        return isNotEmpty(fieldValue) && ((Collection<Publication>) fieldValue).iterator().next() instanceof Publication;
    }

    private boolean isNotEmpty(Object fieldValue) {
        return ((Collection<?>) fieldValue).size() > 0;
    }

    private boolean isCollection(Object object) {
        return object instanceof Collection;
    }
}
