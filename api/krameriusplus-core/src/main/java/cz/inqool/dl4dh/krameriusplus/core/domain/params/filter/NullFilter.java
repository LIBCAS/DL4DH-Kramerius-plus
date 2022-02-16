package cz.inqool.dl4dh.krameriusplus.core.domain.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;

public class NullFilter implements Filter {

    private final String field;

    public NullFilter(@JsonProperty("field") String field) {
        this.field = field;
    }

    @Override
    public Criteria toCriteria() {
        return Criteria.where(field).exists(false);
    }
}
