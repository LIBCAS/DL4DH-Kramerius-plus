package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class AndFilter implements Filter {

    private final List<Filter> filters;

    public AndFilter(@JsonProperty("filters") List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public Criteria toCriteria() {
        Criteria criteria = new Criteria();

        return criteria.andOperator(filters.stream().map(Filter::toCriteria).toArray(Criteria[]::new));
    }

    @Override
    public boolean eval(Object object) throws Exception {
        boolean result = true;

        for (Filter filter : filters) {
            result = result && filter.eval(object);
        }

        return result;
    }
}
