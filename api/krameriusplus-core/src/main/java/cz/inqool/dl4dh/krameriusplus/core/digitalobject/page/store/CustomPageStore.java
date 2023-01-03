package cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomPageStore {

    /**
     * List all pages for given publication. This operation might be costly, so not all the metadata should be
     * returned (mostly tokens should be excluded). To obtain pages with tokens, use the method with Pageable.
     * @param publicationId parent ID
     * @return list of all pages for given publication without tokens
     */
    List<Page> listByPublication(String publicationId);

    /**
     * List all pages for given query.
     * @param query MongoDB Query object
     * @return pages result of pages
     */
    Result<Page> list(Query query);
}

