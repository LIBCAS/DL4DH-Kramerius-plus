package cz.inqool.dl4dh.krameriusplus.core.user.request.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.QUserRequestFile;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRequestFileStore extends DomainStore<UserRequestFile, QUserRequestFile> {

    public UserRequestFileStore(EntityManager entityManager) {
        super(UserRequestFile.class, QUserRequestFile.class, entityManager);
    }
}
