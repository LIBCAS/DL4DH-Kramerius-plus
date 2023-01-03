package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalPartDetails {

    /**
     * E.g. "s. [1] - 329;"
     */
    private String pageRange;

    private String type;

    private String title;

    @JsonProperty("pagenumber")
    private String pageNumber;
}
