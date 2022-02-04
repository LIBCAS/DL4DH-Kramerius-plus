package cz.inqool.dl4dh.krameriusplus.domain.cascade;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPart;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPartStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.PublicationStore;
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
    private final PublicationStore publicationStore;
    private final PageStore pageStore;
    private final InternalPartStore internalPartStore;

    public CascadeCallback(Object source,
                           PublicationStore publicationStore,
                           PageStore pageStore,
                           InternalPartStore internalPartStore) {
        this.source = source;
        this.publicationStore = publicationStore;
        this.pageStore = pageStore;
        this.internalPartStore = internalPartStore;
    }

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
            final Object fieldValue = field.get(getSource());

            if (isNotEmptyCollection(fieldValue)) {
                Collection<?> collectionFieldValue = (Collection<?>) fieldValue;
                if (valuesAreInstanceOf(collectionFieldValue, Publication.class)) {
                    publicationStore.create((Collection<Publication>) collectionFieldValue);
                } else if (valuesAreInstanceOf(collectionFieldValue, InternalPart.class)) {
                    internalPartStore.create((Collection<InternalPart>) collectionFieldValue);
                } else if (valuesAreInstanceOf(collectionFieldValue, Page.class)) {
                    pageStore.create((Collection<Page>) collectionFieldValue);
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
