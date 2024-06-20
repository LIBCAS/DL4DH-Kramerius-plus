package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageCreateDto extends DigitalObjectCreateDto {

    private PageDetails details;

    @JsonProperty("page.type")
    public void setPageType(String pageType) {
        if (pageType == null) {
            return;
        }
        if (details == null) {
            details = new PageDetails();
        }

        details.setType(pageType);
    }
    @JsonProperty("page.number")
    public void setPageNumber(String pageNumber) {
        if (pageNumber == null) {
            return;
        }
        if (details == null) {
            details = new PageDetails();
        }

        details.setPageNumber(pageNumber);
    }

    @Override
    public DigitalObject accept(DigitalObjectMapperVisitor visitor) {
        return visitor.fromCreateDto(this);
    }
}
