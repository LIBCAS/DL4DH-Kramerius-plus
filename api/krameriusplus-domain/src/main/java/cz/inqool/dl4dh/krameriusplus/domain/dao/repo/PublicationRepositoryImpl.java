package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PublicationRepositoryImpl extends AbstractCustomRepository<Publication> {

    @Autowired
    public PublicationRepositoryImpl(MongoTemplate mongoTemplate) {
        super(Publication.class, mongoTemplate);
    }
}
