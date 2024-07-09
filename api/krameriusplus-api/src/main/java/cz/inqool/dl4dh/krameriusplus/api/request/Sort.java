package cz.inqool.dl4dh.krameriusplus.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sort {

    private Order order;

    private Field field;

    public enum Order {
        ASC,
        DESC
    }

    public enum Field {
        CREATED,
        UPDATED,
        AUTHOR,
    }
}
