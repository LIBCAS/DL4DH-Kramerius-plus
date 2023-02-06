package cz.inqool.dl4dh.krameriusplus.api.publication;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicationFacade {

    PublicationDto findPublication(String id);

    List<PublicationDto> listPublicationChildren(String parentId);

    Result<PublicationDto> listPublications(PublicationFilter filter, int page, int pageSize);

    Result<PublicationDto> listPublishedModified(LocalDateTime modifiedAfter, int page, int pageSize);

    PageDto findPage(String id);

    Result<PageDto> listPages(String publicationId, int page, int pageSize);

    void publish(String publicationId);

    void publish(List<String> publicationIds);

    void unpublish(String publicationId);

    void unpublish(List<String> publicationIds);
}
