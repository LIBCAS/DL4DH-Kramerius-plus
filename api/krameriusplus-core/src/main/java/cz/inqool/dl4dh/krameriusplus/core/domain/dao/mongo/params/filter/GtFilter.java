package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;

public class GtFilter extends FieldValueOperation {

    @Getter
    private final String field;

    private final Object value;

    public GtFilter(@JsonProperty("field") String field, @JsonProperty("value") Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Criteria toCriteria() {
        return Criteria.where(field).gt(value);
    }

    @Override
    protected boolean doCompare(Object fieldValue) {
        // same as LtFilter...
        if (fieldValue instanceof Number && value instanceof Number) {
            return ((Number) fieldValue).doubleValue() >= ((Number) value).doubleValue();
        } else if (fieldValue instanceof Instant && value instanceof Instant) {
            return ((Instant) fieldValue).compareTo(((Instant) value)) >= 0;
        } else {
            return false;
        }
    }
}
