package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface CustomPublicationStore {

    List<Publication> findAllPublishedModified(Instant publishedModifiedAfter);

    QueryResults<Publication> findAllChildren(String parentId, Pageable pageRequest);

    List<String> findAllChildrenIds(String parentId);

    QueryResults<Publication> findAll(PublicationListFilterDto filter, Pageable pageRequest);

    QueryResults<Publication> findAll(Params params);

    long countById(String publicationId);
}