package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.Field;

public class RegexFilter implements Filter {

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
    public boolean eval(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getFields();

        for (Field objectField : fields) {
            if (objectField.getName().equals(field)) {
                Object objectFieldValue = objectField.get(object);
                if (objectFieldValue instanceof String) {
                    return ((String) objectFieldValue).matches(value);
                }
            }
        }

        return false;
    }
}
