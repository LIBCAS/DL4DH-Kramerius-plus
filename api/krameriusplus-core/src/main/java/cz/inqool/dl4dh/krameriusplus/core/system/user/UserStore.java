package cz.inqool.dl4dh.krameriusplus.core.system.user;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserStore extends DatedStore<User, QUser> {

    public UserStore() {
        super(User.class, QUser.class);
    }

    public Optional<User> findByUsername(String username) {
        List<User> result = query().select(qObject)
                .where(qObject.username.eq(username))
                .where(qObject.deleted.isNull())
                .fetch();

        detachAll();

        if (result.size() > 1) {
            throw new IllegalStateException("Expected one user with username='"
                    + username + "', found: " + result.size());
        }

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
