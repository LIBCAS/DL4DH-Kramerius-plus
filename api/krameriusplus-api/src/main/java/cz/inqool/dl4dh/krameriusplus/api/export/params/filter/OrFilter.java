package cz.inqool.dl4dh.krameriusplus.api.export.params.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrFilter extends Filter {

    private List<Filter> filters;
}
