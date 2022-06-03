package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PublicationMongoWriter extends MongoItemWriter<Publication> {

    @Autowired
    public PublicationMongoWriter(MongoOperations mongoOperations) {
        setCollection("publications");
        setTemplate(mongoOperations);
    }
}
