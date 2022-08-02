package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page;

import com.querydsl.core.QueryResults;

import java.util.List;

public interface CustomPageStore {
    QueryResults<Page> list(String publicationId, int page, int pageSize);

    List<Page> listWithTei(String publicationId);
}
