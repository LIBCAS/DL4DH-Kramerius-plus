package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import java.lang.reflect.Field;

import static org.springframework.data.mongodb.core.query.Criteria.where;


public class EqFilter implements Filter {

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
    public boolean eval(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field objectField : fields) {
            if (objectField.getName().equals(field)) {
                Object objectFieldValue = objectField.get(object);

                return objectFieldValue.equals(value);
            }
        }

        return false;
    }
}
