package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store;


import com.querydsl.core.types.dsl.EntityPathBase;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.domain.security.user.KrameriusUser;
import cz.inqool.dl4dh.krameriusplus.core.domain.security.user.KrameriusUserStore;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

public abstract class OwnedObjectStore<T extends OwnedObject, Q extends EntityPathBase<T>> extends DatedStore<T, Q> {

    protected KrameriusUserStore krameriusUserStore;

    public OwnedObjectStore(Class<T> type, Class<Q> qType) {
        super(type, qType);
    }

    protected void preCreateHook(T entity) {
        if (entity.getOwner() == null) {
            notNull(SecurityContextHolder.getContext(), () -> new MissingObjectException(SecurityContextHolder.class, null));

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof Principal)) {
                throw new IllegalStateException("SecurityContext principal is not an instance of java.security.Principal");
            }

            String username = ((Principal) principal).getName();
            KrameriusUser krameriusUser = krameriusUserStore.findUserByUsername(username);
            if (krameriusUser == null) {
                krameriusUser = krameriusUserStore.create(new KrameriusUser(username));
            }

            entity.setOwner(krameriusUser);
        }
    }

    @Override
    public T create(@NonNull T entity) {
        preCreateHook(entity);

        return super.create(entity);
    }

    @Autowired
    public void setKrameriusUserStore(KrameriusUserStore krameriusUserStore) {
        this.krameriusUserStore = krameriusUserStore;
    }
}
