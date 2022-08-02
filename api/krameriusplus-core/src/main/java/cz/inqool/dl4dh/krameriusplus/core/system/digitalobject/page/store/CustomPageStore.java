package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import lombok.NonNull;

import java.util.List;

public interface CustomPageStore {
    QueryResults<Page> list(String publicationId, int page, int pageSize);

    List<Page> listWithTei(String publicationId);

    Page find(@NonNull String id);

    Page find(@NonNull String id, List<String> includeFields);

}

