package cz.inqool.dl4dh.krameriusplus.api.export.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Sorting {

    private final String field;

    private final Direction direction;

    public Sorting(@JsonProperty("field") String field, @JsonProperty("direction") Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public enum Direction {
        ASC,
        DESC
    }
}
