package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Norbert Bodnar
 */
@Repository
public class PageRepositoryImpl extends AbstractCustomRepository<Page> {

    @Autowired
    public PageRepositoryImpl(MongoTemplate mongoTemplate) {
        super(Page.class, mongoTemplate);
    }
}
