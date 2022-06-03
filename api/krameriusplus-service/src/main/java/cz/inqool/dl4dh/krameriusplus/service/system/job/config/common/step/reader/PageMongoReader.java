package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@StepScope
public class PageMongoReader extends MongoItemReader<Page> {

    @Autowired
    public PageMongoReader(ObjectMapper objectMapper,
                           MongoOperations mongoOperations,
                           @Value("#{jobParameters['publicationId']}") String publicationId,
                           @Value("#{jobParameters['params']}") String paramsString) throws JsonProcessingException {
        Query query = new Query();
        query.addCriteria(where("parentId").is(publicationId));

        if (paramsString != null) {
            processJobParameters(paramsString, objectMapper, query);
        }

        setName("currentPage");
        setTemplate(mongoOperations);
        setCollection("pages");
        setTargetType(Page.class);
        setPageSize(10);
        setQuery(query);
        setSort(Map.of("index", Sort.Direction.ASC));
    }

    private void processJobParameters(String paramsString, ObjectMapper objectMapper, Query query) throws JsonProcessingException {
        Params params = objectMapper.readValue(paramsString, Params.class);

        if (params.getPaging() != null) {
            setMaxItemCount(params.getPaging().getPageSize());
            setCurrentItemCount(params.getPaging().getPage() * params.getPaging().getPageSize());
        }

        setFields(objectMapper.writeValueAsString(params.toFieldsMap()));
        setSort(params.getSorting().stream().collect(Collectors.toMap(Sorting::getField, Sorting::getDirection)));

        params.getFilters().forEach(filter -> query.addCriteria(filter.toCriteria()));
    }
}
