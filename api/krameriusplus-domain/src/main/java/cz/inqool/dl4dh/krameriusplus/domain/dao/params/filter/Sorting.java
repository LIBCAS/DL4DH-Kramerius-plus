package cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class Sorting {

    private final String field;

    private final Sort.Direction direction;

    public Sorting(@JsonProperty("field") String field, @JsonProperty("direction") Sort.Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public Sort.Order toOrder() {
        return new Sort.Order(direction, field);
    }
}
