package cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomPageStore {

    /**
     * List all pages for given query.
     * @param query MongoDB Query object
     * @return pages result of pages
     */
    Result<Page> list(Query query);
}

