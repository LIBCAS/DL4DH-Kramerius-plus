package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class InFilter extends Filter {

    private String field;

    private Collection<String> values;
}
