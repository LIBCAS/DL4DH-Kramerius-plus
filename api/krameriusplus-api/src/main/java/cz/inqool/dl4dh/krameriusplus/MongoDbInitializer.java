package cz.inqool.dl4dh.krameriusplus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Component;

/**
 * @author Norbert Bodnar
 */
@Component
@ConditionalOnProperty(prefix = "spring.data.mongodb", value = "drop-on-start", havingValue = "true")
public class MongoDbInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final MongoDatabaseFactory mongoDatabaseFactory;

    @Autowired
    public MongoDbInitializer(MongoDatabaseFactory mongoDatabaseFactory) {
        this.mongoDatabaseFactory = mongoDatabaseFactory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        mongoDatabaseFactory.getMongoDatabase().drop();
    }
}
