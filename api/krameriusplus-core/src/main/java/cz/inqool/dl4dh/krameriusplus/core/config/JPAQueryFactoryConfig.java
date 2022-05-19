package cz.inqool.dl4dh.krameriusplus.core.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class JPAQueryFactoryConfig {

    /**
     * Produces QueryDSL {@link JPAQueryFactory} used in {@link cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DomainStore}.
     *
     * @param entityManager Provided JPA {@link EntityManager}
     * @return produced {@link JPAQueryFactory}
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
