package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomPageStore {

    QueryResults<Page> findAllByPublication(String publicationId, Pageable pageRequest);

    QueryResults<Page> findAll(Params params);

    List<Page> findAll(Query query);
}

