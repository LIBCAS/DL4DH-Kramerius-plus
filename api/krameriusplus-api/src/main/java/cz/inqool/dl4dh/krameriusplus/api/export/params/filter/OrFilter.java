package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrFilter extends Filter {

    private final List<Filter> filters;

    public OrFilter(@JsonProperty("filters") List<Filter> filters) {
        this.filters = filters;
    }
}
