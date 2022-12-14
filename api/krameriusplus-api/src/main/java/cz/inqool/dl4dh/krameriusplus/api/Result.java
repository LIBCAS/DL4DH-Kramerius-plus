package cz.inqool.dl4dh.krameriusplus.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Result<T> {

    private final long pageSize;
    private final long page;
    private final long total;
    private final List<T> items;
}
