package cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.filter.Filter;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.filter.Sorting;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.isTrue;

@Getter
@Setter
public class Params {

    /**
     * Paging configuration
     * Not applying, if paging equals to null
     */
    @Setter(AccessLevel.NONE)
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

    public void setPage(int page) {
        if (paging == null) {
            paging = new Paging();
        }

        paging.setPage(page);
    }

    public void setPageSize(int pageSize) {
        if (paging == null) {
            paging = new Paging();
        }

        paging.setPageSize(pageSize);
    }

    public Query toQuery() {
        isTrue(includeFields.isEmpty() || excludeFields.isEmpty(),
                () -> new IllegalArgumentException("Cannot set both inclusion and exclusion parameters"));

        Query query = new Query();
        for (Filter filter : filters) {
            query.addCriteria(filter.toCriteria());
        }

        if (!includeFields.isEmpty()) {
            query.fields().include("_class"); // always include _class field, so MongoDB can deserialize document to POJO
        }

        for (String field : includeFields) {
            query.fields().include(field);
        }

        for (String field : excludeFields) {
            query.fields().exclude(field);
        }

        query.with(toPageRequest());

        return query;
    }

    private Pageable toPageRequest() {
        Sort sort;
        if (sorting.isEmpty()) {
            sort = Sort.by(Sort.Direction.DESC, "created");
        } else {
            sort = Sort.by(getSorting()
                    .stream()
                    .map(Sorting::toOrder)
                    .collect(Collectors.toList()));
        }

        return paging == null ?
                PageRequest.of(0, Integer.MAX_VALUE, sort) :
                PageRequest.of(paging.getPage(), paging.getPageSize(), sort);
    }

    public Map<String, Integer> toFieldsMap() {
        Map<String, Integer> result = new HashMap<>();

        includeFields.forEach(includeField -> result.put(includeField, 1));
        excludeFields.forEach(excludeField -> result.put(excludeField, 0));

        return result;
    }
}
