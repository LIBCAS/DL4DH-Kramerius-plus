package cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.PersistenceException;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static cz.inqool.dl4dh.krameriusplus.api.exception.SystemLogDetails.LogLevel.FATAL_ERROR;
import static cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.PersistenceException.PersistenceErrorCode.QUERY_DSL;

/**
 * Facade around JPA {@link EntityManager} and QueryDSL providing CRUD operations.
 *
 * <p>
 * All the entity instances should have externally set {@link DomainObject#id} to an {@link UUID}
 * </p>
 *
 * <p>
 * JPA concept of managed/detached instances is prone to development errors. Therefore every instance should be
 * detached upon retrieving. All methods in {@link DomainStore} adhere to this rule.
 * </p>
 *
 * <p>
 * After every saving of instance, the {@link EntityManager}'s context is flushed. This is a rather expensive
 * operation.
 * </p>
 *
 * @param <T> Type of entity to hold
 * @param <Q> Type of query object
 */
public abstract class DomainStore<T extends DomainObject, Q extends EntityPathBase<T>> extends SimpleJpaRepository<T, String> {

    /**
     * Entity manager used for JPA
     */
    protected EntityManager entityManager;

    /**
     * QueryDSL query factory
     */
    protected JPAQueryFactory queryFactory;

    /**
     * Entity class object
     */
    @Getter
    protected Class<T> type;

    /**
     * QueryDSL meta class object
     */
    protected Class<Q> qType;

    protected Q qObject;

    public DomainStore(Class<T> type, Class<Q> qType, EntityManager entityManager) {
        super(type, entityManager);
        this.entityManager = entityManager;
        this.type = type;
        this.qType = qType;

        this.qObject = constructQObject(type, qType);
    }

    /**
     * Creates QueryDSL query object.
     */
    protected JPAQuery<?> query() {
        return queryFactory.from(qObject);
    }

    /**
     * Creates meta object attribute.
     * <p>
     * Used for addressing QueryDSL attributes, which are not known at compile time. Should be used with caution,
     * because it circumvents type safety.
     *
     * @param name name of the attribute
     * @return meta object attribute
     */
    protected StringPath propertyPath(String name) {
        PathBuilder<T> builder = new PathBuilder<>(qObject.getType(), qObject.getMetadata().getName());
        return builder.getString(name);
    }

    private Q constructQObject(Class<T> type, Class<Q> qType) {
        String name = type.getSimpleName();
        name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

        try {
            Constructor<Q> constructor = qType.getConstructor(String.class);
            return constructor.newInstance(name);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new PersistenceException("Error creating Q object for + " + type.getName(), QUERY_DSL, FATAL_ERROR);
        }
    }

    @Autowired
    public void setQueryFactory(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

}
