package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;

public interface CustomPublicationStore {
    Publication listWithTei(String publicationId);

    String getTitle(String publicationId);

    List<Publication> listPublishedModified(Instant publishedModifiedAfter);

    QueryResults<Publication> list(String publicationId, int page, int pageSize);

    QueryResults<Publication> list(PublicationListFilterDto filter, int page, int pageSize);

    Publication find(@NonNull String id, List<String> includeFields);

    Publication find(@NonNull String id);
}