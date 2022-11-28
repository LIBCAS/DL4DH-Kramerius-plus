package cz.inqool.dl4dh.krameriusplus.corev2.user;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class KrameriusUserStore extends DatedStore<KrameriusUser, QKrameriusUser> {

    public KrameriusUserStore() {
        super(KrameriusUser.class, QKrameriusUser.class);
    }

    public KrameriusUser findUserByUsername(String username) {
        JPAQuery<KrameriusUser> query = query().select(qObject)
                .where(qObject.username.eq(username))
                .where(qObject.deleted.isNull());

        KrameriusUser result = query.fetchFirst();

        detachAll();

        return result;
    }
}
