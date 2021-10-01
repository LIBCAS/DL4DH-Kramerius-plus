package cz.inqool.dl4dh.krameriusplus.domain.dao.params;

import cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter.Filter;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter.Sorting;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Params {

    private boolean disablePagination = false;

    private int pageOffset = 0;

    private int pageSize = 20;

    private List<Sorting> sort = new ArrayList<>();

    /**
     * List of filters that will be applied. The operator between them is by default AND
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<Filter> filters = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<String> includeFields = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<String> excludeFields = new ArrayList<>();

    public List<Filter> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public Params addFilters(Filter... filter) {
        filters.addAll(Arrays.asList(filter));
        return this;
    }

    public List<String> getIncludeFields() {
        return Collections.unmodifiableList(includeFields);
    }

    public Params includeFields(String... fields) {
        includeFields.addAll(Arrays.asList(fields));
        return this;
    }

    public List<String> getExcludeFields() {
        return Collections.unmodifiableList(excludeFields);
    }

    public Params excludeFields(String... field) {
        excludeFields.addAll(Arrays.asList(field));
        return this;
    }

    public Pageable toPageRequest() {
        Sort sort = Sort.by(getSort()
                .stream()
                .map(Sorting::toOrder)
                .collect(Collectors.toList()));

        return disablePagination ?
                PageRequest.of(0, Integer.MAX_VALUE, sort) : PageRequest.of(pageOffset, pageSize, sort);
    }
}
