package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base for all Field:Value type filters
 *
 * @author Filip Kollar
 */
public abstract class FieldValueOperation implements Filter {

    protected Object getValueFromField(Object object) throws InvocationTargetException, IllegalAccessException {
        String fieldName = getField().replaceAll("_", "");
        Field matchingField = ReflectionUtils.findField(object.getClass(), fieldName);
        if (matchingField == null) {
            return false;
        }
        Method accessor = ReflectionUtils.findMethod(object.getClass(), "get" + StringUtils.capitalize(fieldName));

        return accessor == null ? null : accessor.invoke(object);
    }

    protected abstract String getField();

    protected abstract boolean doCompare(Object object);

    @Override
    public boolean eval(Object object) throws Exception {
        return object != null && doCompare(getValueFromField(object));
    }
}
