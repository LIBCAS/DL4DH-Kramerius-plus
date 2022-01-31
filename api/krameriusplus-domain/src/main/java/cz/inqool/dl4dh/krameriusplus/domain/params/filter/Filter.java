package cz.inqool.dl4dh.krameriusplus.domain.params.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.mongodb.core.query.Criteria;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "operation")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "EQ", value = EqFilter.class)
})
public interface Filter {

    /**
     * Returns a MongoDB Criteria object, which can be chained and used in mongoDb queries
     *
     * @return created criteria object
     */
    Criteria toCriteria();
}
