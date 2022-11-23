package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.AndFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.GtFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.InFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.AbstractMongoStore;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class CustomPublicationStoreImpl extends AbstractMongoStore<Publication> implements CustomPublicationStore {

    public CustomPublicationStoreImpl(MongoOperations mongoOperations) {
        super(mongoOperations, Publication.class);
    }

    @Override
    public QueryResults<Publication> findAllChildren(String parentId, Pageable pageRequest) {
        if (pageRequest.isPaged() && pageRequest.getSort().equals(Sort.unsorted())) {
            pageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "index"));
        }

        Query query = query(where("parentId").is(parentId));

        long total = mongoOperations.count(query, type);

        List<Publication> result = mongoOperations.find(query.with(pageRequest), type);

        return constructQueryResults(result, pageRequest, total);
    }

    @Override
    public List<String> findAllChildrenIds(String parentId) {
        Query query = query(where("parentId").is(parentId));
        query.fields().include("_id", "_class");

        return mongoOperations.find(query, type).stream().map(DomainObject::getId).collect(Collectors.toList());
    }

    @Override
    public QueryResults<Publication> findAll(PublicationListFilterDto filter, Pageable pageRequest) {
        if (pageRequest.isPaged() && pageRequest.getSort().equals(Sort.unsorted())) {
            pageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "created"));
        }

        Query query = new Query();

        if (filter.getTitle() != null) {
            query.addCriteria(where("title").regex(filter.getTitle()));
        }

        if (filter.getParentId() != null) {
            query.addCriteria(where("parentId").is(filter.getParentId()));
        }

        if (filter.getCreatedBefore() != null) {
            query.addCriteria(where("created").lte(filter.getCreatedBefore()));
        }

        if (filter.getCreatedAfter() != null) {
            query.addCriteria(where("created").gte(filter.getCreatedAfter()));
        }

        if (filter.getIsPublished() != null) {
            query.addCriteria(where("publishInfo.isPublished").is(filter.getIsPublished()));
        }

        if (filter.getPublishedBefore() != null) {
            query.addCriteria(where("publishInfo.publishedLastModified").lte(filter.getPublishedBefore()));
        }

        if (filter.getPublishedAfter() != null) {
            query.addCriteria(where("publishInfo.publishedLastModified").gte(filter.getPublishedAfter()));
        }

        long total = mongoOperations.count(query, type);

        List<Publication> result = mongoOperations.find(query.with(pageRequest), type);

        return constructQueryResults(result, pageRequest, total);
    }

    @Override
    public List<Publication> findAllPublishedModified(Instant publishedModifiedAfter) {
        Query query = query(where("publishInfo.publishedLastModified").gte(publishedModifiedAfter));

        return mongoOperations.find(query, type);
    }

    @Override
    public List<String> findAllEditions(String publicationId) {
        List<String> result = mongoOperations.find(new Params() // get publication with id = publicationId if it has pages
                .addFilters(new AndFilter(List.of(
                        new EqFilter("_id", publicationId),
                        new GtFilter("pageCount", 0))))
                .includeFields("_id", "_class")
                .toMongoQuery(false), type)
                .stream()
                .map(DomainObject::getId)
                .collect(Collectors.toList());

        Set<String> parentIds = new LinkedHashSet<>(result);

        // get child publications tree
        while (!parentIds.isEmpty()) {
            Params params = new Params()
                    .addFilters(new AndFilter(List.of(
                            new GtFilter("pageCount", 0),
                            new InFilter("parentId", parentIds))))
                    .includeFields("_id", "_class");

            params.setSorting(List.of(new Sorting("index", Sort.Direction.ASC)));

            parentIds = mongoOperations.find(params.toMongoQuery(false), type)
                    .stream()
                    .map(Publication::getId)
                    .collect(Collectors.toSet());
            result.addAll(parentIds);
        }

        return result;
    }
}


