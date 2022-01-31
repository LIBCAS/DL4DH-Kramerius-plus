package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page;

import cz.inqool.dl4dh.krameriusplus.domain.repo.AbstractCustomRepository;
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
