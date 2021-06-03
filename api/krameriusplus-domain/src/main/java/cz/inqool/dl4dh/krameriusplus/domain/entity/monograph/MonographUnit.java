package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.MONOGRAPH_UNIT;

/**
 * Object representing a MonographUnit. MonographUnits must contain pages directly as children
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographUnit extends Publication {

    private String partNumber;

    private String partTitle;

    private String donator;

    private String parentId;

    @Transient
    private List<Page> pages = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return MONOGRAPH_UNIT;
    }
}
