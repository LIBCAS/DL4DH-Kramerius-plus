package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPageStore {

    QueryResults<Page> findAllByPublication(String publicationId, Pageable pageRequest);

}

