package cz.inqool.dl4dh.krameriusplus.corev2.user;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class KrameriusUserStore extends DatedStore<User, QKrameriusUser> {

    public KrameriusUserStore() {
        super(User.class, QKrameriusUser.class);
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
