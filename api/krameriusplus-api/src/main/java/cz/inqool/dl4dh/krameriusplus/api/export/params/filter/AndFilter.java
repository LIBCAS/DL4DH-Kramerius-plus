package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AndFilter extends Filter {

    private final List<Filter> filters;

    public AndFilter(@JsonProperty("filters") List<Filter> filters) {
        this.filters = filters;
    }
}
