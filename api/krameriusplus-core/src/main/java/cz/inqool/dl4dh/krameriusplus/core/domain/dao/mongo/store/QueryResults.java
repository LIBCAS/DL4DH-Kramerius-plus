package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class QueryResults<T> {

    private final long limit;
    private final long offset;
    private final long total;
    private final List<T> results;

    public QueryResults(List<T> results, Pageable paging, long total) {
        this.results = results;
        this.total = total;

        if (paging.isPaged()) {
            this.limit = paging.getPageSize();
            this.offset = paging.getOffset();
        } else {
            this.limit = -1;
            this.offset = 0;
        }
    }
}
