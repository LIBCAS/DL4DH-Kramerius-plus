package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Filter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Sorting;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.isTrue;

@Getter
@Setter
public class Params {

    /**
     * Paging configuration
     * Not applying, if paging equals to null
     */
    protected Paging paging = null;

    /**
     * Sorting configuration
     * Not applying, if paging equals to null
     */
    protected List<Sorting> sorting = new ArrayList<>();

    /**
     * List of filters that will be applied. The operator between them is by default AND
     */
    protected List<Filter> filters = new ArrayList<>();

    protected List<String> includeFields = new ArrayList<>();

    protected List<String> excludeFields = new ArrayList<>();

    public Params addFilters(Filter... filter) {
        filters.addAll(Arrays.asList(filter));
        return this;
    }

    public Params includeFields(String... fields) {
        includeFields.addAll(Arrays.asList(fields));
        return this;
    }

    public Params excludeFields(String... field) {
        excludeFields.addAll(Arrays.asList(field));
        return this;
    }

    public Query toMongoQuery() {
        return toMongoQuery(true);
    }

    public Query toMongoQuery(boolean withPaging) {
        Query query = new Query();

        for (Filter filter : filters) {
            query.addCriteria(filter.toCriteria());
        }

        this.applyFields(query);

        if (!withPaging) {
            this.applyPaging(query);
        }

        return query;
    }

    private void applyFields(Query query) {
        isTrue(includeFields.isEmpty() || excludeFields.isEmpty(),
                () -> new IllegalArgumentException("Cannot set both inclusion and exclusion parameters"));


        if (!includeFields.isEmpty()) {
            query.fields().include("_class"); // always include _class field, so MongoDB can deserialize document to POJO
        }

        for (String field : includeFields) {
            query.fields().include(field);
        }

        for (String field : excludeFields) {
            query.fields().exclude(field);
        }
    }

    private void applyPaging(Query query) {
        query.with(toPageable());
    }

    private Sort toSort() {
        if (sorting.isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "created");
        } else {
            return Sort.by(getSorting()
                    .stream()
                    .map(Sorting::toOrder)
                    .collect(Collectors.toList()));
        }
    }

    public Pageable toPageable() {
        Sort sort = toSort();

        return paging == null ?
                PageRequest.of(0, Integer.MAX_VALUE, sort) :
                PageRequest.of(paging.getPage(), paging.getPageSize(), sort);
    }
}
