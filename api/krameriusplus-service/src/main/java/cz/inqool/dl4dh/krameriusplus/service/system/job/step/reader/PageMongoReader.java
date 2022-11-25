package cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@StepScope
public class PageMongoReader extends MongoItemReader<Page> {

    @Autowired
    public PageMongoReader(@Value("#{stepExecutionContext['" + PUBLICATION_ID + "']}") String publicationId, MongoOperations mongoOperations)  {
        this.setQuery(Query.query(where("parentId").is(publicationId)));
        this.setTemplate(mongoOperations);
        this.setTargetType(Page.class);
    }
}
