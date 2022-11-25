package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;

public class RegexFilter extends FieldValueOperation {

    @Getter
    private final String field;

    private final String value;

    public RegexFilter(@JsonProperty("field") String field, @JsonProperty("value") String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Criteria toCriteria() {
        return Criteria.where(field).regex(value);
    }

    @Override
    protected boolean doCompare(Object fieldValue) {
        return fieldValue instanceof String && ((String) fieldValue).matches(value);
    }
}
