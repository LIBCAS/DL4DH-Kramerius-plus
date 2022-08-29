package cz.inqool.dl4dh.krameriusplus.core.domain.security.user;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DomainStore;
import org.springframework.stereotype.Repository;

@Repository
public class KrameriusUserStore extends DomainStore<KrameriusUser, QKrameriusUser> {
    public KrameriusUserStore() {
        super(KrameriusUser.class, QKrameriusUser.class);
    }

    public KrameriusUser findUserByUsername(String username) {
        JPAQuery<KrameriusUser> query = query().select(qObject)
                .where(qObject.username.eq(username));

        detachAll();

        return query.fetchFirst();
    }
}
