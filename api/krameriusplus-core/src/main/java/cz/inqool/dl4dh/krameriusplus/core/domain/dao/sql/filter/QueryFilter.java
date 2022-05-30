package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.filter;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DomainObject;

public interface QueryFilter {

    <T extends DomainObject> void apply(JPAQuery<T> jpaQuery);

}
