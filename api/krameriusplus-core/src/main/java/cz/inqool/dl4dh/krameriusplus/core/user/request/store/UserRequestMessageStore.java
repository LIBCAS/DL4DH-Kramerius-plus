package cz.inqool.dl4dh.krameriusplus.core.user.request.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.QUserRequestMessage;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestMessage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRequestMessageStore extends DatedStore<UserRequestMessage, QUserRequestMessage> {

    public UserRequestMessageStore(EntityManager em) {
        super(UserRequestMessage.class, QUserRequestMessage.class, em);
    }
}
