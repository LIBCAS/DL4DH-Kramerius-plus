package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Params {

    /**
     * Paging configuration
     * Not applying, if paging equals to null
     */
    @Valid
    private Paging paging = null;

    /**
     * Sorting configuration
     * Not applying, if paging equals to null
     */
    @Valid
    private Sorting sorting = null;

    /**
     * Filters configuration
     */
    @Valid
    private List<Filter> filters = new ArrayList<>();

    /**
     * Loads all entity fields if set true
     */
    @JsonIgnore
    private boolean lazyLoading = true;

}
