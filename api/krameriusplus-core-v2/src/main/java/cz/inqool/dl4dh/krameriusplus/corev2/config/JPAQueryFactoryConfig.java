package cz.inqool.dl4dh.krameriusplus.corev2.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class JPAQueryFactoryConfig {

    /**
     * Produces QueryDSL {@link JPAQueryFactory} used in {@link DomainStore}.
     *
     * @param entityManager Provided JPA {@link EntityManager}
     * @return produced {@link JPAQueryFactory}
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
