package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Result with pagination
 *
 * @param <T>
 */
public class Result<T> {

    private Page<T> page;

    public Result(Page<T> page) {
        this.page = page;
    }

    public <U> Result<U> map(Function<? super T, ? extends U> converter) {
        return new Result<>(page.map(converter));
    }

    public List<T> getContent() {
        return page.getContent();
    }

    public Map<String, String> getPaging() {
        return Map.of(
            "number", String.valueOf(page.getPageable().getPageNumber()),
            "size", String.valueOf(page.getPageable().getPageSize())
        );
    }

    public long getTotal() {
        return page.getTotalElements();
    }

}
