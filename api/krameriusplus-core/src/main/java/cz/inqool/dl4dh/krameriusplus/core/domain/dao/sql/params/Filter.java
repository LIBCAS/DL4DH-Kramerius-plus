package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    private String field;

    @NotNull
    private Operator operator = Operator.CONTAINS;

    @NotNull
    private String value;

}
