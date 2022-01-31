package cz.inqool.dl4dh.krameriusplus.domain.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import lombok.NonNull;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class AbstractCustomRepository<T extends DomainObject> implements CustomRepository<T> {

    private final MongoTemplate mongoTemplate;

    private final Class<T> clazz;

    public AbstractCustomRepository(Class<T> clazz, MongoTemplate mongoTemplate) {
        this.clazz = clazz;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<T> list(@NonNull Params params) {
        return mongoTemplate.find(params.toQuery(), clazz);
    }
}
