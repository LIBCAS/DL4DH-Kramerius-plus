package cz.inqool.dl4dh.krameriusplus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PageDto extends DigitalObjectDto<Page> implements ParentAware {

    @JsonProperty("root_pid")
    private String rootId;

    //TODO: change to enum?
    private String pageType;

    private String parentId;

    /**
     * Usually page number
     */
    private String title;

    private KrameriusModel model;

    /**
     * Page number with extracted from details field
     */
    private String pageNumber;

    /**
     * Integer representation of page number (removed every non-numeric character from pageNumber)
     */
    private int index;

    //TODO: change to enum?
    private String policy;

    private String textOcr;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        pageType = (String) details.get("type");
        pageNumber = ((String) details.get("pagenumber")).strip();
    }

    @Override
    public Page toEntity() {
        Page page = super.toEntity(new Page());
        page.setRootId(rootId);
        page.setPageType(pageType);
        page.setPageNumber(pageNumber);
        page.setTitle(title);
        page.setPolicy(policy);
        page.setIndex(index);
        page.setParentId(parentId);

        return page;
    }

    @Override
    public Page accept(KrameriusPublicationAssemblerVisitor visitor) {
        return visitor.assemble(this);
    }
}
