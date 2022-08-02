package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store;

import org.springframework.data.mongodb.core.MongoOperations;

public abstract class AbstractMongoStore<T> {
    protected final MongoOperations mongoOperations;

    protected final Class<T> type;

    public AbstractMongoStore(MongoOperations mongoOperations, Class<T> type) {
        this.mongoOperations = mongoOperations;
        this.type = type;
    }
}
