package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.params;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Paging {

    /**
     * Paging current page
     */
    @NotNull
    private int page;

    /**
     * Paging size
     */
    @NotNull
    private int pageSize;

}
