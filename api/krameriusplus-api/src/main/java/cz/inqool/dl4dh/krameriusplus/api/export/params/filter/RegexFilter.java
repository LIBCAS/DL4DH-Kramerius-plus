package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class RegexFilter extends Filter {

    @Getter
    private final String field;

    private final String value;

    public RegexFilter(@JsonProperty("field") String field, @JsonProperty("value") String value) {
        this.field = field;
        this.value = value;
    }
}
