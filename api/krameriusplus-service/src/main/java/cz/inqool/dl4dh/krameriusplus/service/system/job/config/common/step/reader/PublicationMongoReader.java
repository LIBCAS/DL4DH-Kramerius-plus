package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@StepScope
public class PublicationMongoReader extends MongoItemReader<Publication> {

    @Autowired
    public PublicationMongoReader(MongoOperations mongoOperations,
                                  @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId) {
        setName("currentPublication");
        setTemplate(mongoOperations);
        setCollection("publications");
        setTargetType(Publication.class);
        setPageSize(10);
        setQuery(query(where("_id").is(publicationId)));
        setSort(new HashMap<>());
    }
}
