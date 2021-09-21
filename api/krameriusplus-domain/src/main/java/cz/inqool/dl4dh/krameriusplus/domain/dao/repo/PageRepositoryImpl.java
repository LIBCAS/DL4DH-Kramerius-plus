package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.filter.FilterParam;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
public class PageRepositoryImpl implements PageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PageRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Page> listWithProjection(DynamicQuery dynamicQuery) {
        Query query = new Query();

        for (FilterParam filterParam : dynamicQuery.getProjectionParams().getUdPipeParams()) {
            query.fields().include(filterParam.getDbFieldName());
        }

        return mongoTemplate.find(query, Page.class);
    }
}
