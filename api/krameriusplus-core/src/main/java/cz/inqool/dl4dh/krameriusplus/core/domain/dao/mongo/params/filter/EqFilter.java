package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    public boolean eval(Object object) throws InvocationTargetException, IllegalAccessException {
        Field matchingField = ReflectionUtils.findField(object.getClass(), field);
        if (matchingField == null) {
            return false;
        }
        Method accessor = ReflectionUtils.findMethod(object.getClass(), "get" + StringUtils.capitalize(field));

        return accessor != null && accessor.invoke(object).equals(value);
    }
}
