package cz.inqool.dl4dh.krameriusplus.corev2.batch.components;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PublicationMongoWriter extends MongoItemWriter<Publication> {

    @Autowired
    public PublicationMongoWriter(MongoTemplate mongoTemplate) {
        this.setCollection("Publications");
        this.setTemplate(mongoTemplate);
    }
}
