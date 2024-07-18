package cz.inqool.dl4dh.krameriusplus.core.user.request.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.QUserRequestStateAudit;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestStateAudit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRequestStateAuditStore extends DatedStore<UserRequestStateAudit, QUserRequestStateAudit> {

    public UserRequestStateAuditStore(EntityManager entityManager) {
        super(UserRequestStateAudit.class, QUserRequestStateAudit.class, entityManager);
    }
}
