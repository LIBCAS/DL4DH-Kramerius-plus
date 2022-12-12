package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFacade;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFilter;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicationService implements PublicationFacade {

    @Override
    public PublicationDto findPublication(String id) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public List<PublicationDto> listPublicationChildren(String parentId) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Result<PublicationDto> listPublications(PublicationFilter filter, int page, int pageSize) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public List<PublicationDto> listPublishedModified(LocalDateTime modifiedAfter) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public PageDto findPage(String id) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public List<PageDto> listPages(String publicationId) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public void publish(String publicationId) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public void unpublish(String publicationId) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}
