package cz.inqool.dl4dh.krameriusplus.domain.entity;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import org.springframework.data.domain.Pageable;

/**
 * @author Norbert Bodnar
 */
public interface PageVisitable {

    void addPages(PageRepository pageRepository, Pageable pageable);
}
