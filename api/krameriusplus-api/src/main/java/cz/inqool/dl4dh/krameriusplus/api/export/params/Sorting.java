package cz.inqool.dl4dh.krameriusplus.api.export.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sorting {

    private String field;

    private Direction direction;

    public enum Direction {
        ASC,
        DESC
    }
}
