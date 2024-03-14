package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.monograph;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonographUnitDetails {

    private String title;

    @JsonAlias({"part.number.sort"})
    private String partNumber;
}
