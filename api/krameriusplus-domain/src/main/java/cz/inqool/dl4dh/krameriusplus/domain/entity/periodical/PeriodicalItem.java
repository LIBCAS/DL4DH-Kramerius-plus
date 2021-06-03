package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PERIODICAL_ITEM;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalItem extends Publication {

    private String date;

    private String issueNumber;

    private String partNumber;

    private String parentId;

    private String rootId;

    @Transient
    private List<Page> pages = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return PERIODICAL_ITEM;
    }
}
