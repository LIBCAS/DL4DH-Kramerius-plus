package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class EqFilter extends FieldValueOperation {

    @Getter
    private final String field;

    private final Object value;

    public EqFilter(@JsonProperty("field") String field, @JsonProperty("value") Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Criteria toCriteria() {
        return value == null ? where(field).isNull() : where(field).is(value);
    }

    @Override
    protected boolean doCompare(Object object) {
        return object.equals(value);
    }
}
