package cz.inqool.dl4dh.krameriusplus.core.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.reader.ItemReadersConfig.KrameriusReader.PAGE_MONGO_READER_W_TOKENS;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Configuration
public class ItemReadersConfig {

//    @Bean(PAGE_MONGO_READER)
//    @StepScope
    public MongoItemReader<Page> pageMongoReaderWithoutTokens(MongoOperations mongoOperations,
                                                              @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}") String parentId) {
        Query query = query(where("parentId").is(parentId));
        query.with(Sort.by("index").ascending());
        query.fields().exclude("tokens");

        return new MongoItemReaderBuilder<Page>()
                .template(mongoOperations)
                .targetType(Page.class)
                .query(query)
                .sorts(new HashMap<>())
                .pageSize(10)
//                .name(PAGE_MONGO_READER)
                .build();
    }

    @Bean(PAGE_MONGO_READER_W_TOKENS)
    @StepScope
    public MongoItemReader<Page> pageMongoReaderWithTokens(MongoOperations mongoOperations,
                                                 @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}") String parentId) {
        Query query = query(where("parentId").is(parentId));
        query.with(Sort.by("index").ascending());

        return new MongoItemReaderBuilder<Page>()
                .template(mongoOperations)
                .targetType(Page.class)
                .query(query)
                .sorts(new HashMap<>())
                .name(PAGE_MONGO_READER_W_TOKENS)
                .pageSize(10)
                .build();
    }

    public static class KrameriusReader {

        public static final String PAGE_MONGO_READER_W_TOKENS = "PAGE_MONGO_READER_WITH_TOKENS";

        // TODO: Do not use reader without tokens, because it will override existing tokens in DB on save, causing
        // tokens to get deleted
//        public static final String PAGE_MONGO_READER = "PAGE_MONGO_READER_WITHOUT_TOKENS";
    }
}
