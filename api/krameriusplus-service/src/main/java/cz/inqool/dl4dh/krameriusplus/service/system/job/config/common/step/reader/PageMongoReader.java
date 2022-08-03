package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.NUMBER_OF_ITEMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@StepScope
public class PageMongoReader extends MongoItemReader<Page> {

    /**
     * Total number of Page documents, that match the given query
     */
    private final long numberOfItems;

    @Autowired
    public PageMongoReader(ObjectMapper objectMapper,
                           MongoOperations mongoOperations,
                           @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId,
                           @Value("#{jobParameters['" + PARAMS + "']}") String paramsString) throws JsonProcessingException {
        Query query;
        if (paramsString != null) {
            query = paramsToQuery(paramsString, objectMapper);
        } else {
            query = new Query();
        }

        query.addCriteria(where("parentId").is(publicationId));

        setName("currentPage");
        setTemplate(mongoOperations);
        setCollection("pages");
        setTargetType(Page.class);
        setPageSize(10);
        setQuery(query);

        numberOfItems = mongoOperations.count(query, Page.class);
    }

    private Query paramsToQuery(String paramsString, ObjectMapper objectMapper) throws JsonProcessingException {
        Params params = objectMapper.readValue(paramsString, Params.class);

        return params.toMongoQuery();
    }

    @BeforeStep
    public void setNumberOfItems(StepExecution stepExecution) {
        stepExecution.getExecutionContext().putLong(NUMBER_OF_ITEMS, numberOfItems);
    }
}
