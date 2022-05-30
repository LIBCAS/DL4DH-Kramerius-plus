package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sorting {

    /**
     * Sorting attribute
     */
    @NotNull
    private String field;

    /**
     * Sorting order
     */
    @NotNull
    private Order sort;

}
