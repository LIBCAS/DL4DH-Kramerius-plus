package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class PublicationService {

    private PublicationStore publicationStore;

    private PageStore pageStore;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault());


    @Transactional
    public void save(@NonNull Publication publication) {
        publicationStore.save(publication);
    }

    /**
     * Returns the given publication with only teiHeader and teiBody fields
     * @param publicationId
     * @return
     */
    public Publication listTei(String publicationId) {
        Publication result = publicationStore.listWithTei(publicationId);

        result.setPages(pageStore.listWithTei(publicationId));

        return result;
    }

    public List<Publication> listPublishedModified(String publishedModifiedAfter) {
        try {
            Instant instant = Instant.from(formatter.parse(publishedModifiedAfter));

            return publicationStore.listPublishedModified(instant);
        } catch (DateTimeException e) {
            throw new ValidationException("Failed to parse given dateTime", ValidationException.ErrorCode.INVALID_PARAMETERS, e);
        }

    }

    @Transactional
    public void publish(String publicationId) {
        Publication publication = publicationStore.find(publicationId);

        publication.getPublishInfo().publish();

        publicationStore.save(publication);
    }

    @Transactional
    public void unPublish(String publicationId) {
        Publication publication = publicationStore.find(publicationId);

        publication.getPublishInfo().unPublish();

        publicationStore.save(publication);
    }

    /**
     * Returns the publication with given ID with all its fields
     */
    public Publication find(String publicationId) {
        Publication publication = publicationStore.find(publicationId);
        notNull(publication, () -> new MissingObjectException(Publication.class, publicationId));

        return publication;
    }

    public Page findPage(String publicationId, String pageId) {
        Page page = pageStore.find(pageId);
        notNull(page, () -> new MissingObjectException(Page.class, pageId));
        eq(page.getParentId(), publicationId, () -> new MissingObjectException(Page.class, pageId));

        return page;
    }

    public QueryResults<Publication> listChildren(String publicationId, int page, int pageSize) {
        return publicationStore.list(publicationId, page, pageSize);
    }

    public QueryResults<Page> listPages(String publicationId, int page, int pageSize) {
        return pageStore.list(publicationId, page, pageSize);
    }

    public QueryResults<Publication> list(int page, int pageSize) {
        return publicationStore.list(page, pageSize);
    }

    public List<Publication> list(Params params) {
        return publicationStore.listAll(params);
    }

    public String getTitle(String publicationId) {
        return publicationStore.getTitle(publicationId);
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
