package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class GtFilter extends Filter {

    @Getter
    private final String field;

    private final Object value;

    public GtFilter(@JsonProperty("field") String field, @JsonProperty("value") Object value) {
        this.field = field;
        this.value = value;
    }
}
