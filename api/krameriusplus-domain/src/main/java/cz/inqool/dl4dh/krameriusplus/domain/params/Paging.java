package cz.inqool.dl4dh.krameriusplus.domain.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {

    /**
     * Paging current page
     */
    private int page = 0;

    /**
     * Paging size
     */
    private int pageSize = 10;
}
