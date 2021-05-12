package cz.inqool.dl4dh.krameriusplus.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class KrameriusPageDto {

    private String pid;

    private String rootPid;

    //TODO: change to enum?
    private String pageType;

    //TODO: cleanup data (trim extra spaces)
    private String pageNumber;

    private String title;

    //TODO: change to enum?
    private String policy;

    private String textOcr;

    public String getModel() {
        return "page";
    }

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        this.pageType = (String) details.get("type");
        this.pageNumber = ((String) details.get("pagenumber")).trim();
    }

    public Page toEntity() {
        Page page = new Page();
        page.setPid(pid);
        page.setRootId(rootPid);
        page.setPageType(pageType);
        page.setPageNumber(pageNumber);
        page.setTitle(title);
        page.setPolicy(policy);

        return page;
    }
}
