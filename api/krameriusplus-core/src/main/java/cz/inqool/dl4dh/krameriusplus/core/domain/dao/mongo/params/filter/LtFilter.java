package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;

public class LtFilter extends FieldValueOperation {

    @Getter
    private final String field;

    private final Object value;

    public LtFilter(@JsonProperty("field") String field, @JsonProperty("value") Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Criteria toCriteria() {
        return Criteria.where(field).lt(value);
    }

    @Override
    protected boolean doCompare(Object fieldValue) {
        // idk why it needs <= comparison arguments are probably reverse
        if (fieldValue instanceof Number && value instanceof Number) {
            return ((Number) fieldValue).doubleValue() <= ((Number) value).doubleValue();
        }
        else if (fieldValue instanceof Instant && value instanceof Instant) {
            return ((Instant) fieldValue).compareTo(((Instant) value)) <= 0;
        }
        else {
            return false;
        }
    }
}
