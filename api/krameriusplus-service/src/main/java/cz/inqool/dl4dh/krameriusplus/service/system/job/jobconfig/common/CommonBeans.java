package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Configuration
public class CommonBeans {

    @Bean
    @StepScope
    public MongoItemReader<Page> pageReader(ObjectMapper objectMapper,
                                            MongoOperations mongoOperations,
                                            @Value("#{jobParameters['publicationId']}") String publicationId,
                                            @Value("#{jobParameters['params']}") String params) throws JsonProcessingException {

        MongoItemReaderBuilder<Page> readerBuilder =new MongoItemReaderBuilder<>();
        Query query = new Query();
        query.addCriteria(where("parentId").is(publicationId));

        if (params != null) {
            Params queryParams = objectMapper.readValue(params, Params.class);

            if (queryParams.getPaging() != null) {
                readerBuilder.maxItemCount(queryParams.getPaging().getPageSize());
                readerBuilder.currentItemCount(queryParams.getPaging().getPage() * queryParams.getPaging().getPageSize());
            }

            readerBuilder.fields(objectMapper.writeValueAsString(queryParams.toFieldsMap()));
            readerBuilder.sorts(queryParams.getSorting().stream().collect(Collectors.toMap(Sorting::getField, Sorting::getDirection)));

            queryParams.getFilters().forEach(filter -> query.addCriteria(filter.toCriteria()));
        }



        return readerBuilder
                .name("currentPage")
                .template(mongoOperations)
                .collection("pages")
                .targetType(Page.class)
                .pageSize(10)
                .query(query)
                .sorts(Map.of("index", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<Page> pageWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<Page>()
                .collection("pages")
                .template(mongoOperations)
                .build();
    }

    @StepScope
    @Bean
    public MongoItemReader<Publication> publicationReader(MongoOperations mongoOperations,
                                                          @Value("#{jobParameters['publicationId']}") String publicationId) {
        return new MongoItemReaderBuilder<Publication>()
                .name("currentPublication")
                .template(mongoOperations)
                .collection("publications")
                .targetType(Publication.class)
                .pageSize(10)
                .query(query(where("_id").is(publicationId)))
                .sorts(new HashMap<>())
                .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<Publication> publicationWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<Publication>()
                .collection("publications")
                .template(mongoOperations)
                .build();
    }


    @StepScope
    @Bean
    public JsonFileItemWriter<Page> pageJsonWriter(ObjectMapper objectMapper, @Value("#{jobParameters['publicationId']}") String publicationId) {
        return new JsonFileItemWriterBuilder<Page>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>(objectMapper))
                .resource(new FileSystemResource("tmp_" + ExportFormat.JSON.getFileName(publicationId)))
                .name(publicationId)
                .append(true)
                .build();
    }
}
