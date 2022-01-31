package cz.inqool.dl4dh.krameriusplus.service.dataaccess;

import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PublicationRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Autowired
    public PublicationService(PublicationRepository publicationRepository, PageRepository pageRepository) {
        this.publicationRepository = publicationRepository;
        this.pageRepository = pageRepository;
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

    /**
     * Returns the publication with given ID (without also getting its pages)
     */
    public Publication find(String publicationId) {
        return publicationRepository.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication with ID=" + publicationId + " not found"));
    }

    /**
     * Returns a publication with given ID, including its pages depending on the given {@param param}
     */
    public Publication findWithPages(String publicationId, Params params) {
        Publication publication = find(publicationId);

        if (publication instanceof PagesAware) {
            params.addFilters(new EqFilter("parentId", publicationId));

            if (params.getSort().isEmpty()) {
                params.getSort().add(new Sorting("index", Sort.Direction.ASC));
            }
            ((PagesAware) publication).setPages(pageRepository.list(params));
        }

        return publication;
    }

    public List<Publication> list(Params params) {
        return publicationRepository.list(params);
    }
}
