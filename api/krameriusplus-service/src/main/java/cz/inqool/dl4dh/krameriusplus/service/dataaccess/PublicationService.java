package cz.inqool.dl4dh.krameriusplus.service.dataaccess;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PublicationRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;

    private final PageRepository pageRepository;

    public static final PageRequest ALL_PAGES = PageRequest.of(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int DEFAULT_PAGE_SIZE = 10;

    private final int DEFAULT_PAGE_NUMBER = 1;

    @Autowired
    public PublicationService(PublicationRepository publicationRepository, PageRepository pageRepository) {
        this.publicationRepository = publicationRepository;
        this.pageRepository = pageRepository;
    }

    public Publication find(String publicationId) {
        return publicationRepository.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication with ID=" + publicationId + " not found"));
    }

    public Publication findWithPages(String publicationId, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER);
        }
        Publication publication = find(publicationId);

        if (publication instanceof PagesAware) {
            ((PagesAware) publication).setPages(pageRepository.findByParentIdOrderByIndexAsc(publicationId, pageable));
        }

        return publication;
    }

    @Transactional
    public void save(Publication publication) {
        publicationRepository.save(publication);

        for (Publication child : publication.getChildren()) {
            save(child);
        }

        if (publication instanceof PagesAware) {
            pageRepository.saveAll(((PagesAware) publication).getPages());
        }
    }

    @Transactional
    public void save(List<Page> pages) {
        pageRepository.saveAll(pages);
    }

    public List<Publication> list() {
        return publicationRepository.listLight();
    }
}
