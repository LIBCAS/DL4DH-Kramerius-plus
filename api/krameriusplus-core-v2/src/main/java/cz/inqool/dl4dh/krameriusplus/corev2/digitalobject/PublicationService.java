package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFacade;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFilter;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.DigitalObjectMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicationService implements PublicationFacade {

    private PublicationStore publicationStore;

    private DigitalObjectMapper mapper;

    private PageStore pageStore;

    @Override
    public PublicationDto findPublication(String id) {
        Publication publication = publicationStore.findById(id)
                .orElseThrow(() -> new MissingObjectException(Publication.class, id));

        return publication.accept(mapper);
    }

    @Override
    public List<PublicationDto> listPublicationChildren(String parentId) {
        List<Publication> publications = publicationStore.findAllChildren(parentId);

        return publications.stream().map(publication -> publication.accept(mapper)).collect(Collectors.toList());
    }

    @Override
    public Result<PublicationDto> listPublications(PublicationFilter filter, int page, int pageSize) {
        Result<Publication> result = publicationStore.list(filter, page, pageSize);

        return toDtoResult(result);
    }

    @Override
    public Result<PublicationDto> listPublishedModified(LocalDateTime modifiedAfter, int page, int pageSize) {
        PublicationFilter filter = new PublicationFilter();
        filter.setPublishedAfter(modifiedAfter.toInstant(ZoneOffset.UTC));
        Result<Publication> result = publicationStore.list(filter, page, pageSize);

        return toDtoResult(result);
    }

    @Override
    public PageDto findPage(String id) {
        Page page = pageStore.findById(id).orElseThrow(() -> new MissingObjectException(Page.class, id));

        return page.accept(mapper);
    }

    @Override
    public List<PageDto> listPages(String publicationId) {
        List<Page> pages = pageStore.listByPublication(publicationId);

        return pages.stream().map(page -> page.accept(mapper)).collect(Collectors.toList());
    }

    @Override
    public void publish(String publicationId) {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        publication.getPublishInfo().publish();

        publicationStore.save(publication);
    }

    @Override
    public void unpublish(String publicationId) {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        publication.getPublishInfo().unPublish();

        publicationStore.save(publication);
    }

    private Result<PublicationDto> toDtoResult(Result<Publication> publications) {
        return new Result<>(
                publications.getPageSize(),
                publications.getPage(),
                publications.getTotal(),
                publications.getItems()
                        .stream()
                        .map(publication -> publication.accept(mapper))
                        .collect(Collectors.toList()));
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setPageStore(PageStore pageStore) {
        this.pageStore = pageStore;
    }

    @Autowired
    public void setMapper(DigitalObjectMapper mapper) {
        this.mapper = mapper;
    }
}
