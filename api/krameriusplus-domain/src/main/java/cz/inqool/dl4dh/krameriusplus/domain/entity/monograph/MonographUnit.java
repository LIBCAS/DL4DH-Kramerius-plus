package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Pageable;

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
public class MonographUnit extends Publication implements ParentAware {

    private String partNumber;

    private String partTitle;

    private String donator;

    private String parentId;

    private int index;

    @Transient
    @JsonIgnore
    private List<Page> pages = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return MONOGRAPH_UNIT;
    }

    @Override
    public void addPages(PageRepository pageRepository, Pageable pageable) {
        pages = pageRepository.findByParentIdOrderByIndexAsc(id, pageable);
    }

    @Override
    public List<? extends Publication> getChildren() {
        return new ArrayList<>();
    }
}
