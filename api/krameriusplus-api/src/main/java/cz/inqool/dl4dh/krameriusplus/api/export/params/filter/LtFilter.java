package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LtFilter extends Filter {

    private String field;

    private Object value;
}
