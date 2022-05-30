package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class OrFilter implements Filter {

    private final List<Filter> filters;

    public OrFilter(@JsonProperty("filters") List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public Criteria toCriteria() {
        Criteria criteria = new Criteria();

        return criteria.orOperator(filters.stream().map(Filter::toCriteria).toArray(Criteria[]::new));
    }
}
