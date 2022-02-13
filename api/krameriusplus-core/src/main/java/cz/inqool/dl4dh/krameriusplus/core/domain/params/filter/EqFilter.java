package cz.inqool.dl4dh.krameriusplus.core.domain.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;

public class EqFilter implements Filter {

    private final String field;
    private final Object value;

    public EqFilter(@JsonProperty("field") String field, @JsonProperty("value") Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Criteria toCriteria() {
        return Criteria.where(field).is(value);
    }
}
