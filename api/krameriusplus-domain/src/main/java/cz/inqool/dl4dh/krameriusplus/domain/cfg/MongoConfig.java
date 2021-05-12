package cz.inqool.dl4dh.krameriusplus.domain.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * @author Norbert Bodnar
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "KrameriusPlus";
    }
}
