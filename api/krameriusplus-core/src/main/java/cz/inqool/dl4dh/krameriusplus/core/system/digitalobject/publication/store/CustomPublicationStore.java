package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface CustomPublicationStore {

    List<Publication> findAllPublishedModified(Instant publishedModifiedAfter);

    QueryResults<Publication> findAllChildren(String parentId, Pageable pageRequest);

    QueryResults<Publication> findAll(PublicationListFilterDto filter, Pageable pageRequest);
}