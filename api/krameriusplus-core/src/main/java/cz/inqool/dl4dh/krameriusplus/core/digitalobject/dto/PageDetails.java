package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDetails {

    @JsonAlias({"page.type"})
    private String type;

    @JsonAlias({"pagenumber", "page.number"})
    private String pageNumber;
}
