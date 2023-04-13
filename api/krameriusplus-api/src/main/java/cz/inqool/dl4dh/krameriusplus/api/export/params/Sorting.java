package cz.inqool.dl4dh.krameriusplus.api.export.params;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Sorting {

    private String field;

    private Direction direction;

    public enum Direction {
        ASC,
        DESC
    }
}
