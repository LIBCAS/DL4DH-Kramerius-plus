package cz.inqool.dl4dh.krameriusplus.core.domain.params;

import cz.inqool.dl4dh.krameriusplus.core.domain.params.filter.Sorting;
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
        paging = params.getPaging();
        sorting = params.getSorting();
        filters = params.getFilters();
        includeFields = params.getIncludeFields();
        excludeFields = params.getExcludeFields();
    }

    public Params cleanForTei() {
        sorting = new ArrayList<>();
        sorting.add(new Sorting("index", Sort.Direction.ASC));

        includeFields = new ArrayList<>();
        includeFields("id", "teiBody");

        return this;
    }
}
