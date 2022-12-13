package cz.inqool.dl4dh.krameriusplus.corev2.user;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class UserStore extends DatedStore<User, QUser> {

    public UserStore() {
        super(User.class, QUser.class);
    }

    public User findUserByUsername(String username) {
        JPAQuery<User> query = query().select(qObject)
                .where(qObject.username.eq(username))
                .where(qObject.deleted.isNull());

        User result = query.fetchFirst();

        detachAll();

        return result;
    }
}
