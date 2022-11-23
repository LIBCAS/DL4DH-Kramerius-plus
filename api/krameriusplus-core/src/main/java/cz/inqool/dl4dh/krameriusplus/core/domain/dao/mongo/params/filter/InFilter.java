package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collection;

public class InFilter extends FieldValueOperation {

    @Getter
    private final String field;

    private final Collection<String> values;

    public InFilter(@JsonProperty("field") String field, @JsonProperty("values") @NonNull Collection<String> values) {
        this.field = field;
        this.values = values;
    }

    @Override
    public Criteria toCriteria() {
        return Criteria.where(field).in(values);
    }

    @Override
    protected boolean doCompare(Object fieldValue) {
        if (!(fieldValue instanceof String)) {
            return false;
        }

        return values.contains((String) fieldValue);
    }
}
