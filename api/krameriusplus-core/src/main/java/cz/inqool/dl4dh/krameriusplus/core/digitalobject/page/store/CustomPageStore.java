package cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomPageStore {

    /**
     * List all pages for given query.
     * @param query MongoDB Query object
     * @return pages result of pages
     */
    List<Page> list(Query query);

    /**
     * List paged documents
     * @param publicationId ID of parent for which to list pages
     * @param page page
     * @param pageSize pageSize
     * @return Paged result
     */
    Result<Page> list(String publicationId, int page, int pageSize);
}

