package cz.inqool.dl4dh.krameriusplus.api.publication;

import cz.inqool.dl4dh.krameriusplus.api.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicationFacade {

    PublicationDto findPublication(String id);

    List<PublicationDto> listPublicationChildren(String parentId);

    QueryResults<PublicationDto> listPublications(PublicationFilter filter, int page, int pageSize);

    List<PublicationDto> listPublishedModified(LocalDateTime modifiedAfter);

    PageDto findPage(String id);

    List<PageDto> listPages(String publicationId);

    void publish(String publicationId);

    void unpublish(String publicationId);

}
