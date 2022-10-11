package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;

public class GtFilter implements Filter {

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
    public boolean eval(Object object) throws InvocationTargetException, IllegalAccessException {
        Field matchingField = ReflectionUtils.findField(object.getClass(), field);
        if (matchingField == null) {
            return false;
        }
        Method accessor = ReflectionUtils.findMethod(object.getClass(), "get" + StringUtils.capitalize(field));

        return accessor != null && doCompare(accessor.invoke(object));
    }

    private boolean doCompare(Object objectFieldValue) {
        if (objectFieldValue instanceof Number && value instanceof Number) {
            return ((Number) objectFieldValue).doubleValue() > ((Number) value).doubleValue();
        } else if (objectFieldValue instanceof Instant && value instanceof Instant) {
            return ((Instant) objectFieldValue).compareTo(((Instant) value)) > 0;
        } else {
            return false;
        }
    }
}
