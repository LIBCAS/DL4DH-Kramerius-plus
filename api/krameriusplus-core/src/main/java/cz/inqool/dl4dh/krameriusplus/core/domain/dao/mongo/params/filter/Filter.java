package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.mongodb.core.query.Criteria;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "operation")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "EQ", value = EqFilter.class),
        @JsonSubTypes.Type(name = "OR", value = OrFilter.class),
        @JsonSubTypes.Type(name = "GT", value = GtFilter.class),
        @JsonSubTypes.Type(name = "AND", value = AndFilter.class),
        @JsonSubTypes.Type(name = "LT", value = LtFilter.class),
        @JsonSubTypes.Type(name = "NULL", value = NullFilter.class),
        @JsonSubTypes.Type(name = "REGEX", value = RegexFilter.class),
        @JsonSubTypes.Type(name = "IN", value = InFilter.class)
})
public interface Filter {

    /**
     * Returns a MongoDB Criteria object, which can be chained and used in mongoDb queries
     *
     * @return created criteria object
     */
    Criteria toCriteria();

    /**
     * Evaluates filter against an item in Processor. Used to filter items obtained from different sources than
     * MongoDB (e.g. from Kramerius)
     * @param object item in ItemProcessor
     * @return true if item should be kept, false otherwise
     */
    boolean eval(Object object) throws Exception;
}
