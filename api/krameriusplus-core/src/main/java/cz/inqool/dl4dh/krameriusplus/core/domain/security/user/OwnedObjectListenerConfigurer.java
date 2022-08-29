package cz.inqool.dl4dh.krameriusplus.core.domain.security.user;


import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Component
public class OwnedObjectListenerConfigurer {

    @Autowired
    private OwnedObjectCreationListener listener;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);

        sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class).appendListeners(EventType.PRE_INSERT, listener);
    }
}
