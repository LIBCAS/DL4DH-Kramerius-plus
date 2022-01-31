package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart;

import cz.inqool.dl4dh.krameriusplus.domain.repo.AbstractCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InternalPartRepositoryImpl extends AbstractCustomRepository<InternalPart> {

    @Autowired
    public InternalPartRepositoryImpl(MongoTemplate mongoTemplate) {
        super(InternalPart.class, mongoTemplate);
    }
}
