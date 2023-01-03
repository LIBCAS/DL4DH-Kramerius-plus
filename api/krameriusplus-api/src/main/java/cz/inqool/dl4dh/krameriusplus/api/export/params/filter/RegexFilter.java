package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegexFilter extends Filter {

    private String field;

    private String value;
}
