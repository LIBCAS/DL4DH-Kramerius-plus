package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params;

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

    private String value;

}
