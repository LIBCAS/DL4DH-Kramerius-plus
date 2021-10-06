package cz.inqool.dl4dh.krameriusplus.domain.dao.params;

import cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter.Filter;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter.Sorting;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Params {

    protected boolean disablePagination = false;

    protected int pageOffset = 0;

    protected int pageSize = 20;

    protected List<Sorting> sort = new ArrayList<>();

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

    public Pageable toPageRequest() {
        Sort sort = Sort.by(getSort()
                .stream()
                .map(Sorting::toOrder)
                .collect(Collectors.toList()));

        return disablePagination ?
                PageRequest.of(0, Integer.MAX_VALUE, sort) : PageRequest.of(pageOffset, pageSize, sort);
    }
}
