package cz.inqool.dl4dh.krameriusplus.core.domain.security.user;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class OwnedObjectCreationListener implements PreInsertEventListener {

    private final KrameriusUserStore store;

    @Autowired
    public OwnedObjectCreationListener(KrameriusUserStore store) {
        this.store = store;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        Object entity = event.getEntity();

        if (entity instanceof OwnedObject) {
            OwnedObject ownedObject = (OwnedObject) entity;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof KeycloakPrincipal)) {
                throw new IllegalStateException("Principal is not instance of keycloak principal");
            }

            String username = ((KeycloakPrincipal<?>) principal).getName();
            KrameriusUser krameriusUser = store.findUserByUsername(username);
            if (krameriusUser == null) {
                krameriusUser = store.create(new KrameriusUser(username));
            }

            ownedObject.setOwner(krameriusUser);
        }

        return false;
    }
}
