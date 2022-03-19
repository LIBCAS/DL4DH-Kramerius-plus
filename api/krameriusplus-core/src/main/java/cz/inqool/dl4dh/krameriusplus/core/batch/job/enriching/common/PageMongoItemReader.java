package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@StepScope
public class PageMongoItemReader extends MongoItemReader<Page> {

    @Autowired
    public PageMongoItemReader(@Value("#{jobParameters['publicationId']}") String publicationId,
                               MongoOperations mongoOperations) {
        setCollection("pages");
        setQuery(new Query().addCriteria(where("parentId").is(publicationId)));
        setTemplate(mongoOperations);
        setPageSize(10);
        setTargetType(Page.class);
    }
}
