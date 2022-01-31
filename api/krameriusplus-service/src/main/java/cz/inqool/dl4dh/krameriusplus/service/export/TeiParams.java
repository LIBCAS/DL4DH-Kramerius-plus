package cz.inqool.dl4dh.krameriusplus.service.export;

import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.params.filter.Sorting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeiParams extends Params {

    private List<String> udPipeParams = new ArrayList<>();

    private List<String> nameTagParams = new ArrayList<>();

    private List<String> altoParams = new ArrayList<>();

    public TeiParams(Params params) {
        disablePagination = params.isDisablePagination();
        pageOffset = params.getPageOffset();
        pageSize = params.getPageSize();
        sort = params.getSort();
        filters = params.getFilters();
        includeFields = params.getIncludeFields();
        excludeFields = params.getExcludeFields();
    }

    public Params cleanForTei() {
        sort = new ArrayList<>();
        sort.add(new Sorting("index", Sort.Direction.ASC));

        includeFields = new ArrayList<>();
        includeFields("id", "teiBody");

        return this;
    }
}
