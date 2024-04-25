package cz.inqool.dl4dh.krameriusplus.core.user.request.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.QUserRequestPart;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestPart;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRequestPartStore extends DomainStore<UserRequestPart, QUserRequestPart> {

    public UserRequestPartStore(EntityManager entityManager) {
        super(UserRequestPart.class, QUserRequestPart.class, entityManager);
    }
}
