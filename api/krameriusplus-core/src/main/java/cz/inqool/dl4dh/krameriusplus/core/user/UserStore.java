package cz.inqool.dl4dh.krameriusplus.core.user;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserStore extends DatedStore<User, QUser> {

    public UserStore(EntityManager em) {
        super(User.class, QUser.class, em);
    }

    public User findUserByUsername(String username) {
        return query().select(qObject)
                .where(qObject.username.eq(username))
                .where(qObject.deleted.isNull())
                .fetchOne();
    }
}
