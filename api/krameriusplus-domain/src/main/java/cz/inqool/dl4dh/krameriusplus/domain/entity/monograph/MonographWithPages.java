package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithPages extends Monograph {

    @Transient
    private List<Page> pages = new ArrayList<>();

    @Override
    public void addPages(PageRepository pageRepository, Pageable pageable) {
        pages = pageRepository.findByParentIdOrderByIndexAsc(id, pageable);
    }

    @Override
    public List<? extends Publication> getChildren() {
        return new ArrayList<>();
    }
}
