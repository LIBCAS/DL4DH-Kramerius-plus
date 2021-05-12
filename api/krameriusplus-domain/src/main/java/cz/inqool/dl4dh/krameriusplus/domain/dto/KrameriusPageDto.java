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

    @JsonProperty("root_pid")
    private String rootPid;

    //TODO: change to enum?
    private String pageType;

    /**
     * Usually page number
     */
    private String title;

    /**
     * Page number with extracted from details field
     */
    private String pageNumber;

    /**
     * Integer representation of page number (removed every non-numeric character from pageNumber)
     */
    private int pageIndex;

    //TODO: change to enum?
    private String policy;

    private String textOcr;

    public String getModel() {
        return "page";
    }

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        pageType = (String) details.get("type");
        pageNumber = ((String) details.get("pagenumber")).strip();
        try {
            pageIndex = Integer.parseInt(pageNumber.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            // do nothing;
        }
    }

    public Page toEntity() {
        Page page = new Page();
        page.setPid(pid);
        page.setRootId(rootPid);
        page.setPageType(pageType);
        page.setPageNumber(pageNumber);
        page.setTitle(title);
        page.setPolicy(policy);
        page.setPageIndex(pageIndex);

        return page;
    }
}
