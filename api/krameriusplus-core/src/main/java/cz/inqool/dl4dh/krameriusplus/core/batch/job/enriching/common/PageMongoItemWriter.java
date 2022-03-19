package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PageMongoItemWriter extends MongoItemWriter<Page> {

    @Autowired
    public PageMongoItemWriter(MongoOperations mongoOperations) {
        setCollection("pages");
        setTemplate(mongoOperations);
    }
}
