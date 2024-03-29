package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDetails {

    private String type;

    @JsonProperty("pagenumber")
    private String pageNumber;
}
