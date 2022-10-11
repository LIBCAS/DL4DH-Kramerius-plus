package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ReflectionUtils;

public class NullFilter implements Filter {

    private final String field;

    public NullFilter(@JsonProperty("field") String field) {
        this.field = field;
    }

    @Override
    public Criteria toCriteria() {
        return Criteria.where(field).exists(false);
    }

    @Override
    public boolean eval(Object object) {
        return ReflectionUtils.findField(object.getClass(), field) == null;
    }
}
