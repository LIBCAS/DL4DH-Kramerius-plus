package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class PublicationService {

    private PublicationStore publicationStore;

    private PageStore pageStore;

    public Publication save(@NonNull Publication publication) {
        publication = publicationStore.save(publication);
        pageStore.save(publication.getPages());

        return publication;
    }

    /**
     * Returns the publication with given ID with all its fields
     */
    public Publication find(String publicationId) {
        Publication publication = publicationStore.find(publicationId);
        Utils.notNull(publication, () -> new MissingObjectException(Publication.class, publicationId));

        return publication;
    }

    /**
     * Returns a publication with given ID, including its pages depending on the given {@param param}
     */
    public Publication findWithPages(String publicationId, Params pageParams) {
        Publication publication = find(publicationId);

        pageParams.addFilters(new EqFilter("parentId", publicationId));

        if (pageParams.getSorting().isEmpty()) {
            pageParams.getSorting().add(new Sorting("index", Sort.Direction.ASC));
        }

        publication.setPages(pageStore.listAll(pageParams));

        return publication;
    }

    public List<Publication> list(Params params) {
        return publicationStore.listAll(params);
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setPageStore(PageStore pageStore) {
        this.pageStore = pageStore;
    }
}
