package cz.inqool.dl4dh.krameriusplus.core.user.request.store;

import com.querydsl.jpa.impl.JPAQuery;
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

    public Page<UserRequest> findAllForUser(Pageable pageable, String id, boolean viewDeleted) {
        JPAQuery<?> queryBase = query()
                .from(qObject)
                .where(qObject.user.id.eq(id));

        return listUserRequests(pageable, viewDeleted, queryBase);
    }

    public Page<UserRequest> findAll(Pageable pageable, boolean viewDeleted) {
        JPAQuery<?> queryBase = query()
                .from(qObject);

        return listUserRequests(pageable, viewDeleted, queryBase);
    }

    private Page<UserRequest> listUserRequests(Pageable pageable, boolean viewDeleted, JPAQuery<?> queryBase) {
        if (!viewDeleted) {
            queryBase = queryBase.where(qObject.deleted.isNull());
        }

        long count = queryBase.select(qObject.count()).fetchFirst();

        List<UserRequest> requests = queryBase
                .select(qObject)
                .offset(pageable.getOffset() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(requests, pageable, count);
    }
}
