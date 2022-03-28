package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.filter;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DomainObject;

public interface QueryFilter {

    <T extends DomainObject> void apply(JPAQuery<T> jpaQuery);

}
