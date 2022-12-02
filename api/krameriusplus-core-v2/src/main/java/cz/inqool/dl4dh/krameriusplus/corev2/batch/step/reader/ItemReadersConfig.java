package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Configuration
public class ItemReadersConfig {

    @Bean
    @StepScope
    public MongoItemReader<Page> pageMongoReader(MongoOperations mongoOperations,
                                                 @Value("#{jobParameters['PUBLICATION_ID']}") String parentId) {
        Query query = query(where("parentId").is(parentId));
        query.fields().exclude("tokens");
        query.with(Sort.by("index").ascending());

        return new MongoItemReaderBuilder<Page>()
                .template(mongoOperations)
                .targetType(Page.class)
                .query(query)
                .pageSize(10)
                .build();
    }
}
