package cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
public class Sorting {

    private final String field;

    private final Sort.Direction direction;

    public Sort.Order toOrder() {
        return new Sort.Order(direction, field);
    }
}
