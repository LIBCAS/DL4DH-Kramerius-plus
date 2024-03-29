package cz.inqool.dl4dh.krameriusplus.core.digitalobject;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFacade;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFilter;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.domain.document.DomainDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Result<PageDto> listPages(String publicationId, int page, int pageSize) {
        return toDtoPagesResult(pageStore.list(publicationId, page, pageSize));
    }

    @Override
    public void publish(String publicationId) {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        publication.getPublishInfo().publish();

        publicationStore.save(publication);

        List<Publication> children = publicationStore.findAllChildren(publicationId);
        publish(children.stream().map(DomainDocument::getId).collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public void publish(List<String> publicationIds) {
        publicationIds.forEach(this::publish);
    }

    @Override
    public void unpublish(String publicationId) {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        publication.getPublishInfo().unPublish();

        publicationStore.save(publication);

        List<Publication> children = publicationStore.findAllChildren(publicationId);
        unpublish(children.stream().map(DomainDocument::getId).collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public void unpublish(List<String> publicationIds) {
        publicationIds.forEach(this::unpublish);
    }

    private Result<PublicationDto> toDtoResult(Result<Publication> publications) {
        return new Result<>(
                publications.getPage(),
                publications.getPageSize(),
                publications.getTotal(),
                publications.getItems()
                        .stream()
                        .map(publication -> publication.accept(mapper))
                        .collect(Collectors.toList()));
    }

    private Result<PageDto> toDtoPagesResult(Result<Page> pages) {
        return new Result<>(
                pages.getPage(),
                pages.getPageSize(),
                pages.getTotal(),
                pages.getItems()
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
