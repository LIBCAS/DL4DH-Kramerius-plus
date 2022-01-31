package cz.inqool.dl4dh.krameriusplus.domain.cascade;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPart;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPartRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.PublicationRepository;
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

    private final Object source;
    private final PublicationRepository publicationRepository;
    private final PageRepository pageRepository;
    private final InternalPartRepository internalPartRepository;

    public CascadeCallback(Object source,
                           PublicationRepository publicationRepository,
                           PageRepository pageRepository,
                           InternalPartRepository internalPartRepository) {
        this.source = source;
        this.publicationRepository = publicationRepository;
        this.pageRepository = pageRepository;
        this.internalPartRepository = internalPartRepository;
    }

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
            final Object fieldValue = field.get(getSource());

            if (isNotEmptyCollection(fieldValue)) {
                Collection<?> collectionFieldValue = (Collection<?>) fieldValue;
                if (valuesAreInstanceOf(collectionFieldValue, Publication.class)) {
                    publicationRepository.saveAll((Collection<Publication>) collectionFieldValue);
                } else if (valuesAreInstanceOf(collectionFieldValue, InternalPart.class)) {
                    internalPartRepository.saveAll((Collection<InternalPart>) collectionFieldValue);
                } else if (valuesAreInstanceOf(collectionFieldValue, Page.class)) {
                    pageRepository.saveAll((Collection<Page>) collectionFieldValue);
                }
            }
        }
    }

    private boolean valuesAreInstanceOf(Collection<?> fieldValue, Class<?> clazz) {
        return fieldValue.stream().allMatch(clazz::isInstance);
    }

    private boolean isNotEmptyCollection(Object fieldValue) {
        return isCollection(fieldValue) && isNotEmpty(fieldValue);
    }

    private boolean isNotEmpty(Object fieldValue) {
        return ((Collection<?>) fieldValue).size() > 0;
    }

    private boolean isCollection(Object object) {
        return object instanceof Collection;
    }
}
