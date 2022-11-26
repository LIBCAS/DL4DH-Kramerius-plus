package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

public class InFilter extends Filter {

    @Getter
    private final String field;

    private final Collection<String> values;

    public InFilter(@JsonProperty("field") String field, @JsonProperty("values") @NonNull Collection<String> values) {
        this.field = field;
        this.values = values;
    }
}
