package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NullFilter extends Filter {

    private final String field;

    public NullFilter(@JsonProperty("field") String field) {
        this.field = field;
    }
}
