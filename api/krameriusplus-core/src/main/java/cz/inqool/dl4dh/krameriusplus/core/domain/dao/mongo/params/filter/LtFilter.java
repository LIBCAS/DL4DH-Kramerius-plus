package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.Field;

public class LtFilter implements Filter {

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
    public boolean eval(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field objectField : fields) {
            if (objectField.getName().equals(field)) {
                Object objectFieldValue = objectField.get(object);
                if (objectFieldValue instanceof Number && value instanceof Number) {
                    return ((Number) objectFieldValue).doubleValue() < ((Number) value).doubleValue();
                }
            }
        }

        return false;
    }
}
