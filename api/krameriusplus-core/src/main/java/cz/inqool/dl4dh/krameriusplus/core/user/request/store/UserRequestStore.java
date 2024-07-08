package cz.inqool.dl4dh.krameriusplus.core.user.request.store;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.api.request.ListFilterDto;
import cz.inqool.dl4dh.krameriusplus.api.request.Sort;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.QUserRequest;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserRequestStore extends DatedStore<UserRequest, QUserRequest> {

    public UserRequestStore(EntityManager em) {
        super(UserRequest.class, QUserRequest.class, em);
    }

    public Page<UserRequest> findAllForUser(Pageable pageable, ListFilterDto filters, String id, boolean viewDeleted) {
        JPAQuery<?> queryBase = query()
                .from(qObject)
                .where(qObject.user.id.eq(id));

        return listUserRequests(pageable, viewDeleted, queryBase, filters);
    }

    public Page<UserRequest> findAll(Pageable pageable, ListFilterDto filters, boolean viewDeleted) {
        JPAQuery<?> queryBase = query()
                .from(qObject);

        return listUserRequests(pageable, viewDeleted, queryBase, filters);
    }

    private Page<UserRequest> listUserRequests(Pageable pageable, boolean viewDeleted, JPAQuery<?> queryBase, ListFilterDto filters) {
        if (!viewDeleted) {
            queryBase = queryBase.where(qObject.deleted.isNull());
        }

        addFilters(filters, queryBase);

        long count = queryBase.select(qObject.count()).fetchFirst();

        addSort(filters, queryBase);

        List<UserRequest> requests = queryBase
                .select(qObject)
                .offset(pageable.getOffset() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(requests, pageable, count);
    }

    private void addFilters(ListFilterDto filters, JPAQuery<?> queryBase) {
        if (filters.getRootFilterOperation().equals(ListFilterDto.RootFilterOperation.AND)) {
            queryBase.where(createAndFilters(filters));
        } else {
            queryBase.where(createOrFilters(filters));
        }
    }

    private void addSort(ListFilterDto filters, JPAQuery<?> queryBase) {
        switch (filters.getField()) {
            case AUTHOR:
                queryBase.orderBy(filters.getOrder().equals(Sort.Order.ASC) ? qObject.user.username.asc() : qObject.user.username.desc());
                return;
            case CREATED:
                queryBase.orderBy(filters.getOrder().equals(Sort.Order.ASC) ? qObject.created.asc() : qObject.created.desc());
                return;
            case UPDATED:
                queryBase.orderBy(filters.getOrder().equals(Sort.Order.ASC) ? qObject.updated.asc() : qObject.updated.desc());
        }
    }

    private BooleanBuilder createAndFilters(ListFilterDto filters) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (filters.getType() != null) {
            booleanBuilder.and(qObject.type.eq(filters.getType()));
        }
        if (filters.getYear() != null) {
            booleanBuilder.and(qObject.created.year().eq(filters.getYear()));
        }
        if (filters.getIdentification() != null) {
            booleanBuilder.and(qObject.identification.eq(filters.getIdentification()));
        }
        if (filters.getState() != null) {
            booleanBuilder.and(qObject.state.eq(filters.getState()));
        }
        if (filters.getUsername() != null) {
            booleanBuilder.and(qObject.user.username.containsIgnoreCase(filters.getUsername()));
        }

        return booleanBuilder;
    }

    private BooleanBuilder createOrFilters(ListFilterDto filters) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (filters.getType() != null) {
            booleanBuilder.or(qObject.type.eq(filters.getType()));
        }
        if (filters.getYear() != null) {
            booleanBuilder.or(qObject.created.year().eq(filters.getYear()));
        }
        if (filters.getIdentification() != null) {
            booleanBuilder.or(qObject.identification.eq(filters.getIdentification()));
        }
        if (filters.getState() != null) {
            booleanBuilder.or(qObject.state.eq(filters.getState()));
        }
        if (filters.getUsername() != null) {
            booleanBuilder.or(qObject.user.username.containsIgnoreCase(filters.getUsername()));
        }

        return booleanBuilder;
    }
}
